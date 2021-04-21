let index = {
	init: function() {
		$("#btn-save").on("click", () => {	//function(){}, () => {} this를 바인딩하기 위해서!!
			this.save();
		});
		
		$("#btn-login").on("click", () => {	//function(){}, () => {} this를 바인딩하기 위해서!!
			this.login();
		});
	},
	
	save: function() {
		//alert("user의 save함수 호출됨");
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		}

		//ajax호출시 default가 비동기 호출!!
		//ajax통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!!!
		//ajax가 통신을 성공하고 서버가 json으로 값을 리턴해주면 자동으로 자바스크립트 오브젝트로 변환해준다!!
		$.ajax({
			type: "POST",
			url: "/blog/api/user",
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8 ",	//body데이터가 어떤 타입인지(MIME)
			dataType : "json"	//요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열이다 ( 응답이 json이면 ) => 자동으로 javascript Object로 변경해준다..!
		}).done(function(resp) {
			location.href="/blog";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		}); 
	},
	
	login: function() {
		//alert("user의 save함수 호출됨");
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
		}

		$.ajax({
			type: "POST",
			url: "/blog/api/user/login",
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8 ",	//body데이터가 어떤 타입인지(MIME)
			dataType : "json"	//요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열이다 ( 응답이 json이면 ) => 자동으로 javascript Object로 변경해준다..!
		}).done(function(resp) {
			alert("로그인이 완료되었습니다!");
			location.href="/blog";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		}); 
	}
	
	
}

index.init();