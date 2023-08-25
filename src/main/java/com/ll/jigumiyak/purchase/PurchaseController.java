package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import com.ll.jigumiyak.util.RsData;
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
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/purchase")
@RequiredArgsConstructor
@Controller
public class PurchaseController {

    @Value("${custom.toss.payments.secret-key}")
    private String secretKey;

    private final PurchaseService purchaseService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String purchase(@RequestParam List<Long> cartItemId,
                           PurchaseForm purchaseForm,
                           Model model, Principal principal) {

        log.info("cartItem size: " + cartItemId.size());

        SiteUser purchaser = this.userService.getUserByLoginId(principal.getName());

        model.addAttribute("purchaser", purchaser);

        return "purchase/purchase";
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/payment/before")
    @ResponseBody
    public ResponseEntity beforePayment(@Valid PurchaseForm purchaseForm, BindingResult bindingResult, Principal principal) {

        log.info("purchaserName: " + purchaseForm.getPurchaserName());
        log.info("purchaserPhoneNumber: " + purchaseForm.getPurchaserPhoneNumber());
        log.info("receiverName: " + purchaseForm.getReceiverName());
        log.info("receiverPhoneNumber: " + purchaseForm.getReceiverPhoneNumber());
        log.info("receiverAddress.zoneCode: " + purchaseForm.getReceiverAddress().getZoneCode());
        log.info("receiverAddress.mainAddress: " + purchaseForm.getReceiverAddress().getMainAddress());
        log.info("receiverAddress.subAddress: " + purchaseForm.getReceiverAddress().getSubAddress());
        log.info("deliveryRequest: " + purchaseForm.getDeliveryRequest());
        log.info("customDeliveryRequest: " + purchaseForm.getCustomDeliveryRequest());

        SiteUser purchaser = this.userService.getUserByLoginId(principal.getName());

        Map<String, Object> purchaseAttributes = new HashMap<>();

        String purchaseId = "jigumiyak_" + String.format("%010d", purchaser.getId()) + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_n"));
        String purchaseName = "김상엽 처리의 건";

        purchaseAttributes.put("amount", 50000);
        purchaseAttributes.put("orderId", purchaseId);
        purchaseAttributes.put("orderName", purchaseName);
        purchaseAttributes.put("customerName", purchaseForm.getPurchaserName());

        log.info("purchaseAttributes: {}", purchaseAttributes);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "주문 ID를 생성했습니다", purchaseAttributes));
    }

    @PreAuthorize("isAuthenticated")
    @PostMapping("/payment/cancel")
    @ResponseBody
    public ResponseEntity cancelPayment(@RequestParam("orderId") String purchaseId, Principal principal) {

        log.info("payment cancel on purchaseId == " + purchaseId);

        SiteUser purchaser = this.userService.getUserByLoginId(principal.getName());

//        Purchase purchase = this.purchaseService.getPurchaseByPurchaserAndPurchaseId(purchaser, orderId);
//
//        this.purchaseService.deletePurchase(purchase);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "해당 주문 ID를 삭제했습니다", ""));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment/success")
    public String successPayment(Model model,
                                 @RequestParam("orderId") String purchaseId,
                                 @RequestParam("amount") Integer amount,
                                 @RequestParam("paymentKey") String paymentKey) throws Exception {

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

        model.addAttribute("method", (String) jsonObject.get("method"));
        model.addAttribute("orderName", (String) jsonObject.get("orderName"));

        if (((String) jsonObject.get("method")) != null) {
            if (((String) jsonObject.get("method")).equals("카드")) {
                model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
            } else if (((String) jsonObject.get("method")).equals("가상계좌")) {
                model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
            } else if (((String) jsonObject.get("method")).equals("계좌이체")) {
                model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
            } else if (((String) jsonObject.get("method")).equals("휴대폰")) {
                model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
            }
        } else {
            model.addAttribute("code", (String) jsonObject.get("code"));
            model.addAttribute("message", (String) jsonObject.get("message"));
        }

        return "purchase/success";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment/fail")
    public String failPayment(Model model, Principal principal,
                              @RequestParam("orderId") String purchaseId,
                              @RequestParam("message") String message,
                              @RequestParam("code") String code) throws Exception {

        log.info("payment fail on purchaseId == " + purchaseId);

        SiteUser purchaser = this.userService.getUserByLoginId(principal.getName());

        Purchase purchase = this.purchaseService.getPurchaseByPurchaserAndPurchaseId(purchaser, purchaseId);

        this.purchaseService.deletePurchase(purchase);

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "purchase/fail";
    }
}
