$(function() {
    loadData();
});

 var urlParams = new URLSearchParams(window.location.search);
  var gamePlayerId = urlParams.get('Gp');

   const loadData = () => {
      $.get("/api/game_view/" + gamePlayerId)
        .done(function (data) {
          console.log(data);
          renderGamePlayers(data);
          renderShips(data);
          showSalvoes(data);
        })
        .fail(function (jqXHR, textStatus) {
          console.log("Failed: " + textStatus);
        });
    }

 const createGrid = (size, gridId, gridType) => {

    let gridContainer = document.querySelector('.' + gridId);

    for (let i = 0; i < size; i++) {
      let row = document.createElement('div');

      let rowId = String.fromCharCode(i + 64).toLowerCase();

      gridContainer.appendChild(row);

      for (let j = 0; j < size; j++) {
        // Creates a div (cell) for each row.
        let cell = document.createElement('div');
        cell.classList.add('grid-cell');
        if (i > 0 && j > 0) {
          //example: id="salvog5" / id="shipc3"
          cell.id = gridType + rowId + j;
        }
        if (j === 0 && i > 0) {
          // Adds header's column name.
          cell.classList.add('grid-header');
          cell.innerText = String.fromCharCode(i + 64);
        }
        if (i === 0 && j > 0) {
          // Adds header's row name.
          cell.classList.add('grid-header');
          cell.innerText = j;
        }
        row.appendChild(cell)
      }
    }
}

      const renderGamePlayers = data => {

        var playerOne = document.getElementById('playerOne');
        var playerTwo = document.getElementById('playerTwo');

        if (data.gamePlayers[0].id == gamePlayerId) {
          playerOne.innerText = data.gamePlayers[0].player.email;
          playerTwo.innerText = data.gamePlayers[1].player.email;
        } else {
          playerOne.innerText = data.gamePlayers[1].player.email;
          playerTwo.innerText = data.gamePlayers[0].player.email;
        }


      }

        const renderShips = data => {
          data.ships.forEach(shipData => {
            shipData.locations.forEach(location => {
              addShipClass(location);
            })
          });
        }
        const addShipClass = cellId => {
          let shipPart = document.getElementById("ship" + cellId.toLowerCase());

          shipPart.classList.add('ship-placed');
        }
        // Display salvoes for the player and opponent.

function showSalvoes(data) {

    let turnoAux = data.salvoEs;
    let playerOne = turnoAux.filter(x => (x.player.id === Number(getParameterByName('Gp')) ));
    console.log(playerOne);
    let playerTwo = turnoAux.filter(x => (x.player.id !== Number(getParameterByName('Gp')) ));
    playerOne.forEach(turno => {
        turno.location.forEach(bomb => {
                        displayMagic(bomb,"enemy")
                    })
    })
    playerTwo.forEach(turno => {
        turno.location.forEach(bomb => {
                        displayMagic(bomb,"mine")
                    })
    })
};

function displayMagic(hit, board) {
    let type;
    if (board === "mine") {
        type = "ship";
    } else if(board === "enemy") {
        type = "salvo";
    }
    console.log(type);
    console.log(hit);
    let bombId = document.getElementById(type + hit.toLowerCase())
    console.log(bombId)
    bombId.classList.add('salvo-placed')
}

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};


  createGrid(11,"grid-ships","ship");
  createGrid(11, "grid-salvoes", "salvo");


