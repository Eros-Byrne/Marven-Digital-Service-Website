async function leave(leaveButtonElement){
    let teamID = leaveButtonElement.getAttribute("data-teamid");

    let data = {};
    let shouldDelete = confirm("Are you sure you want to leave: " + leaveButtonElement.getAttribute("data-team-name"));
    if(shouldDelete) {
        data[leaveButtonElement.getAttribute("data-csrf-js-name")] = leaveButtonElement.getAttribute("data-csrf-js");
        data["teamID"] = teamID;
        fetch('/teams/leave', {
            "method": "POST",
            "referrer": "http://localhost:8080/teams",
            "headers": {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            "body": new URLSearchParams(data)
        }).then((response) => {
            if (response.ok) {
                response.text().then((json) => {
                    if(json == "Successfully left team"){
                        window.location.reload();
                    }else if(json == "You are the last manager"){
                        alert("You can't leave a group if you are the last manager");
                    }
                });
            }
        });
    }
}

let leaveTeamElements = document.getElementsByClassName("leaveTeamButton")
for(let i = 0; i < leaveTeamElements.length; i++){
    leaveTeamElements.item(i).addEventListener("click", (event) => {
        leave(leaveTeamElements.item(i));
    })
}