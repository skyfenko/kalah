var user2Houses = {};
user2Houses["south"] = [0, 1, 2, 3, 4, 5];
user2Houses["north"] = [7, 8, 9, 10, 11, 12];

function get(k) {
    return user2Houses[k];
}


$('#logout').click(function () {
    $.ajax({
        type: 'POST',
        url: '/logout',
        success: function (data) {
            window.location.href = "/login";
        }
    });
});

csrfValue = $('#csrf').val();


$.ajaxPrefilter(function (options, originalOptions, jqXHR) {
    jqXHR.setRequestHeader('X-CSRF-Token', csrfValue);
});

var chosenHouse;

$('#myModal').on('shown.bs.modal', function (e) {
    var userName = $("#user").val();
    var $takeTurnPlayer = $("#takeTurnPlayer");

    var $modalSubmit = $("#modalSubmit");
    if (userName === $takeTurnPlayer.text()) {
        $modalSubmit.prop("disabled", false);

        chosenHouse = $(e.relatedTarget).data("id");


        var userHouses = get(userName);

        if ($.inArray(chosenHouse, userHouses, 0) !== -1) {
            $("#modalMessage").text("You have chosen " + chosenHouse + " house to move. Do you want to proceed?");
        } else {
            $("#modalMessage").text("You have chosen " + chosenHouse + " house, which you don't own. Try to move again.");
            $modalSubmit.prop("disabled", true)
        }
    } else {
        $("#modalMessage").text("Now is " + $takeTurnPlayer.text() + "'s turn! Please wait for your turn.");
        $modalSubmit.prop("disabled", true)
    }
});

$('#modalSubmit').click(function () {
    $.ajax({
        type: 'POST',
        url: '/api/kalah/turns',
        data: JSON.stringify({
            'takeTurnPlayer': $("#takeTurnPlayer").text(),
            'currentTurn': chosenHouse
        }),
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            for (i = 0; i < data.board.houses.length; i++) {
                $("#" + i).text(data.board.houses[i]);
            }
            $("#takeTurnPlayer").text(data.takeTurnPlayer);
            if (data.winner !== null) {
                $("#winner").text(data.winner);
            }
        },
        error: function (data) {
            alert("error : " + data.error);
        }
    });
});

$("#startGame").click(function () {
    $("#takeTurnPlayer").text("south");

    var $winner = $("#winner");

    if ($winner.text() !== "-") {
        restartGame();
        $winner.text("-")
    } else {
        for (i = 0; i < 14; i++) {
            if (i !== 6 && i !== 13) {
                $("#" + i).text(6);
            } else {
                $("#" + i).text(0);
            }
        }
    }
    disableStartGameButton($(this));
});

function disableStartGameButton(button) {
    button.prop("disabled", true);
    button.removeClass("btn-warning").addClass("btn-error");
    button.text("Game has been started!");
}

function enableStartGameButton(button) {
    button.prop("disabled", false);
    button.addClass("btn-warning").removeClass("btn-error");
    button.text("Start game!");
}

$("#restartGame").click(function () {
    restartGame();
    enableStartGameButton($("#startGame"))
});

function restartGame() {
    $.ajax({
        type: 'POST',
        url: '/api/kalah/game/restart',
        success: function (data) {
            for (i = 0; i < data.board.houses.length; i++) {
                $("#" + i).text(data.board.houses[i]);
            }
            $("#takeTurnPlayer").text(data.takeTurnPlayer);

            if (data.winner !== null) {
                $("#winner").text(data.winner);
            }
        },
        error: function (data) {
            alert(data.error);
        }
    });
}

function fillBoardWithData() {
    $.ajax({
        type: 'GET',
        url: '/api/kalah/game',
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            for (i = 0; i < data.board.houses.length; i++) {
                $("#" + i).text(data.board.houses[i]);
            }
            $("#takeTurnPlayer").text(data.takeTurnPlayer);
            if (data.winner !== null) {
                $("#winner").text(data.winner);
            }
            if (data.started && !data.over) {
                disableStartGameButton($("#startGame"))
            }

            if (data.over) {
                alert("Game is over! Restart game if you want to play again!")
            }
        },
        error: function (data) {
            alert(data.error);
        }
    });
}



