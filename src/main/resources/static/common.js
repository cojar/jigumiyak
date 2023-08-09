function _anonymous(uri, isAuthenticated) {
    if (isAuthenticated === 'false') {
        if(confirm("로그인이 필요한 서비스입니다.\n확인을 누르시면 로그인 페이지로 이동합니다.")) {
            location.href = uri;
        }
    } else {
        if (uri !== '/user/login') {
            location.href = uri;
        }
    }
}