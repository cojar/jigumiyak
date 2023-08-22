function anonymous(uri, isAuthenticated) {
    if (isAuthenticated === 'false') {
        if (confirm("로그인이 필요한 서비스입니다.\n확인을 누르시면 로그인 페이지로 이동합니다.")) {
            location.href = uri;
        }
    } else {
        console.log("해치웠나");

        var countInput = document.getElementById('countSelect');
        var selectedCount = parseInt(countInput.value);

        if (isNaN(selectedCount) || selectedCount < 1) {
            alert("올바른 수량을 입력해주세요.");
            return;
        }

        $("#cartItemForm").submit();
    }
}
