fetch('/api/games')
  .then(function (response) {
    return response.json();
  })
  .then(function (data) {
    if (data.player == "Guest") {
      $("#login-form").show();

      $("#logout-form").hide();
      $("#createGameForm").hide();
    } else {
      $("#userLog").append("Hola: " + data.player.email + "Bienvenido");
      $("#login-form").hide();
      $("#logout-form").show();
      $("#createGameForm").show();
    }

    console.log(data);


  })


$("#login").click(function (evt) {
  login(evt);
})

function login(e) {
  e.preventDefault();
  $.post("/api/login", {
      name: $("#username").val(),
      password: $("#password").val()

    }).done(function () {
      $("#logout-form").show(),
      $("#createGameForm").show();
        $("#password").val(""),
      alert("Hola: " + $('#username').val());
      location.reload();
      $("#login-form").hide();
      $("#logout-form").show();
      $("#createGameForm").show();
    })
    .fail(function () {
      console.log("Failed to LogIn");
      alert("Error")
    })
}

$("#logout").click(function (evt) {
  logout(evt);
})

function logout(e) {
  e.preventDefault();
  $.post("/api/logout")
    .done(function () {
        console.log("Bye");
      alert("Bye : " + $('#username').val());
      location.reload();
      $("#logout-form").hide();
      $("#createGameForm").hide();
      $("#login-form").show()

    })
    .fail(function () {
      alert("Failed to LogOut")
    });
}

$("#signUp").click(function (evt) {
  signUp(evt);
})

function signUp(e) {
  e.preventDefault();
  $.post("/api/players", {
      email: $("#username").val(),
      password: $("#password").val()
    })
    .done(function () {
      console.log("Bienvenido a Salvo");
      login(e);
      leaderboard();
      $("#login-form").hide(),
        $("#logout-form").show(),
        $("#createGameForm").show();
        $("#password").val("")
    })
    .fail(function () {
      alert("Failed to LogIn");
      alert("User not registered")
    });
}

$(document).ready(function () {
  $('#show').mousedown(function () {
    $('#password').removeAttr('type');
    $('#show').addClass('fa-eye').removeClass('fa-eye-slash')

  });

  $('#show').mouseup(function () {
    $('#password').attr('type', 'password');
    $('#show').addClass('fa-eye-slash').removeClass('fa-eye')
  });
});