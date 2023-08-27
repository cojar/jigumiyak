package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.address.Address;
import com.ll.jigumiyak.address.AddressService;
import com.ll.jigumiyak.cart.Cart;
import com.ll.jigumiyak.cart.CartService;
import com.ll.jigumiyak.cart_item.CartItem;
import com.ll.jigumiyak.cart_item.CartItemService;
import com.ll.jigumiyak.purchase_item.PurchaseItem;
import com.ll.jigumiyak.purchase_item.PurchaseItemService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import com.ll.jigumiyak.util.RsData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/purchase")
@RequiredArgsConstructor
@Controller
public class PurchaseController {

    @Value("${custom.toss.payments.secret-key}")
    private String secretKey;

    private final PurchaseService purchaseService;
    private final PurchaseItemService purchaseItemService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final AddressService addressService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String purchase(@RequestParam List<Long> cartItemId,
                           PurchaseForm purchaseForm,
                           Model model, Principal principal) {

        log.info("cartItem size: " + cartItemId.size());

        SiteUser purchaser = this.userService.getUserByLoginId(principal.getName());
        model.addAttribute("purchaser", purchaser);

        List<CartItem> cartItemList = new ArrayList<>();
        int totalAmount = 0;

        for (Long id : cartItemId) {
            CartItem cartItem = this.cartItemService.getCartItem(id);
            cartItemList.add(this.cartItemService.getCartItem(id));
            log.info(cartItem.getProduct().getName() + ": " + cartItem.getCount());
            totalAmount += cartItem.getCount() * cartItem.getProduct().getPrice();
        }

        model.addAttribute("cartItemList", cartItemList);

        log.info(String.format("totalAmount: %,d원", totalAmount));
        model.addAttribute("totalAmount", totalAmount);

        return "purchase/purchase";
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/payment/before")
    @ResponseBody
    public ResponseEntity beforePayment(HttpServletRequest request,
                                        @Valid PurchaseForm purchaseForm, BindingResult bindingResult,
                                        @RequestParam List<Long> cartItemId,
                                        Principal principal) {

        // 주문 실패 시 되돌아갈 주문 페이지 쿼리스트링 생성 및 저장
        request.getSession().removeAttribute("purchaseQuery");
        String purchaseQuery = cartItemId.stream().map(x -> "cartItemId=" + x).collect(Collectors.joining("&"));
        request.getSession().setAttribute("purchaseQuery", purchaseQuery);

        log.info("purchaserName: " + purchaseForm.getPurchaserName());
        log.info("purchaserPhoneNumber: " + purchaseForm.getPurchaserPhoneNumber());
        log.info("receiverName: " + purchaseForm.getReceiverName());
        log.info("receiverPhoneNumber: " + purchaseForm.getReceiverPhoneNumber());
        log.info("receiverAddress.zoneCode: " + purchaseForm.getReceiverAddress().getZoneCode());
        log.info("receiverAddress.mainAddress: " + purchaseForm.getReceiverAddress().getMainAddress());
        log.info("receiverAddress.subAddress: " + purchaseForm.getReceiverAddress().getSubAddress());
        log.info("deliveryRequest: " + purchaseForm.getDeliveryRequest());
        log.info("customDeliveryRequest: " + purchaseForm.getCustomDeliveryRequest());
        log.info("cartItemList: " + cartItemId.toString());

        // 주문 객체 저장 정보 생성
        SiteUser purchaser = this.userService.getUserByLoginId(principal.getName());

        String purchaseId = "jigumiyak_" + String.format("%010d", purchaser.getId()) + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_n"));

        List<CartItem> cartItemList = new ArrayList<>();
        Integer totalAmount = 0;

        for (Long id : cartItemId) {
            CartItem cartItem = this.cartItemService.getCartItem(id);
            cartItemList.add(cartItem);
            log.info(cartItem.getProduct().getName() + ": " + cartItem.getCount());
            totalAmount += cartItem.getCount() * cartItem.getProduct().getPrice();
        }

        String purchaseName = cartItemList.get(0).getProduct().getName();

        if (cartItemList.size() > 1) {
            purchaseName += String.format(" 외 %s건", cartItemList.size() - 1);
        }

        Address receiverAddress = this.addressService.create(purchaseForm.getReceiverAddress().getZoneCode(),
                purchaseForm.getReceiverAddress().getMainAddress(),
                purchaseForm.getReceiverAddress().getSubAddress());

        String deliveryRequest = !purchaseForm.getDeliveryRequest().equals("직접입력") ? purchaseForm.getDeliveryRequest() : purchaseForm.getCustomDeliveryRequest();

        // 주문 객체 저장
        Purchase purchase = this.purchaseService.create(
                purchaseId,
                purchaseName,
                purchaseForm.getPurchaserName(),
                "010" + purchaseForm.getPurchaserPhoneNumber(),
                purchaseForm.getReceiverName(),
                "010" + purchaseForm.getReceiverPhoneNumber(),
                receiverAddress,
                deliveryRequest,
                totalAmount,
                purchaser
        );

        // 주문 아이템 객체 저장
        for (CartItem cartItem : cartItemList) {
            PurchaseItem purchaseItem = this.purchaseItemService.create(cartItem, purchase);
        }

        // 주문 객체 정보 전달 맵 생성
        Map<String, Object> purchaseAttributes = new HashMap<>();

        purchaseAttributes.put("amount", purchase.getTotalAmount());
        purchaseAttributes.put("orderId", purchase.getPurchaseId());
        purchaseAttributes.put("orderName", purchase.getPurchaseName());
        purchaseAttributes.put("customerName", purchase.getPurchaserName());

        log.info("purchaseAttributes: {}", purchaseAttributes);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "주문 ID를 생성했습니다", purchaseAttributes));
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/payment/cancel")
    @ResponseBody
    public ResponseEntity cancelPayment(HttpServletRequest request,
                                        @RequestParam("orderId") String purchaseId, Principal principal) {

        request.getSession().removeAttribute("purchaseQuery");

        log.info("payment cancel on purchaseId == " + purchaseId);

        SiteUser purchaser = this.userService.getUserByLoginId(principal.getName());

        Purchase purchase = this.purchaseService.getPurchaseByPurchaserAndPurchaseId(purchaser, purchaseId);

        if (purchase == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "해당 주문 ID가 존재하지 않습니다", ""));
        }

        this.purchaseService.delete(purchase);
        this.addressService.delete(purchase.getReceiverAddress());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "해당 주문 ID를 삭제했습니다", ""));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment/success")
    public String successPayment(HttpServletRequest request, Model model,
                                 @RequestParam("orderId") String purchaseId,
                                 @RequestParam("amount") Integer amount,
                                 @RequestParam("paymentKey") String paymentKey,
                                 Principal principal) throws Exception {

        request.getSession().removeAttribute("purchaseQuery");

        // 결제 승인 처리
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        JSONObject obj = new JSONObject();
        obj.put("orderId", purchaseId);
        obj.put("amount", amount);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;
        model.addAttribute("isSuccess", isSuccess);

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        log.info("payment success on purchaseId == " + purchaseId);

        // 필요 결제 정보 저장
        model.addAttribute("responseStr", jsonObject.toJSONString());
        log.info(jsonObject.toJSONString());

        String method = jsonObject.get("method").toString();

        String approvedAt = jsonObject.get("approvedAt").toString();
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(approvedAt);
        LocalDateTime payDate = zonedDateTime.toLocalDateTime();

        Integer suppliedAmount = Integer.parseInt(jsonObject.get("suppliedAmount").toString());
        Integer vat = Integer.parseInt(jsonObject.get("vat").toString());

        String paymentDetail = "";
        if (method.equals("간편결제")) {
            paymentDetail = ((JSONObject) jsonObject.get("easyPay")).get("provider").toString();
        } else if (method.equals("카드")) {
            String ownerType = ((JSONObject) jsonObject.get("card")).get("ownerType").toString();
            String cardType = ((JSONObject) jsonObject.get("card")).get("cardType").toString();
            String number = ((JSONObject) jsonObject.get("card")).get("number").toString();
            paymentDetail = ownerType + "_" + cardType + "_" + number;
        }

        SiteUser purchaser = this.userService.getUserByLoginId(principal.getName());
        Purchase purchase = this.purchaseService.getPurchaseByPurchaserAndPurchaseId(purchaser, purchaseId);
        this.purchaseService.updateSuccess(purchase, paymentKey, method, payDate, suppliedAmount, vat, paymentDetail);

        // 구매 완료된 장바구니 아이템 삭제
        List<PurchaseItem> purchaseItemList = purchase.getPurchaseItemList();
        Cart cart = this.cartService.getCartByOwner(purchaser);
        for (PurchaseItem purchaseItem : purchaseItemList) {
            CartItem cartItem = this.cartItemService.getCartItemByProductAndCart(purchaseItem.getProduct(), cart);
            this.cartItemService.delete(cartItem);
        }

        model.addAttribute("code", (String) jsonObject.get("code"));
        model.addAttribute("message", (String) jsonObject.get("message"));

        return "purchase/success";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment/fail")
    public String failPayment(HttpServletRequest request,
                              Model model, Principal principal,
                              @RequestParam("orderId") String purchaseId,
                              @RequestParam("message") String message,
                              @RequestParam("code") String code) throws Exception {

        String purchaseQuery = request.getSession().getAttribute("purchaseQuery").toString();
        request.getSession().removeAttribute("purchaseQuery");

        log.info("payment fail on purchaseId == " + purchaseId);

        SiteUser purchaser = this.userService.getUserByLoginId(principal.getName());

        Purchase purchase = this.purchaseService.getPurchaseByPurchaserAndPurchaseId(purchaser, purchaseId);

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        // 주문 객체 없을 시 잘못된 주문 과정이므로 장바구니로 리턴
        if (purchase == null) {
            model.addAttribute("uri", "/cart");
        }

        this.purchaseService.delete(purchase);
        this.addressService.delete(purchase.getReceiverAddress());

        // 주문 실패 시 실패 창 잠깐 띄우고 바로 주문 페이지로 리턴
        model.addAttribute("uri", "/purchase?" + purchaseQuery);

        return "purchase/fail";
    }
}
