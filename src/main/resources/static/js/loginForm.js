window.addEventListener("load",function(){
    const loginBtn = document.querySelector("#login_btn");
    loginBtn.onclick=function(e){
        let usernameInput = document.querySelector('#username');
        let passwordInput = document.querySelector('#password');
        let username = usernameInput.value;
        let password = passwordInput.value;

        function after(response){
            let headers = response.headers;
            let authorization = headers.get("Authorization");
            console.log(authorization);
            fetch("http://localhost:8080/api/v1/user", {
                method: "GET",
                headers: {
                    "Accept": "*/*",
                    "Content-Type": "application/json",
                    "Connection": "keep-alive",
                    "Authorization": authorization
                }
            }).then((response) => console.log(response));
        }

        if(username.length>1 && password.length > 1){
            fetch("http://localhost:8080/login", {
                method: "POST",
                headers: {
                    "Accept": "*/*",
                    "Content-Type": "application/json",
                    "Connection": "keep-alive"
                },
                body: JSON.stringify({
                    "username": username,
                    "password": password
                }),
            }).then((response) => after(response));
        }
    }
});