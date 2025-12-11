async function populateJoinCode(joinCodeDisplayElement){
    let teamID = joinCodeDisplayElement.getAttribute("data-teamid");
    const response = await fetch('/teams/joinCode/' + teamID, {
        method: "GET"
    });

    if(response.ok){
        joinCodeDisplayElement.value = (await (await response).json());
    }
}

async function resetJoinCode(resetCodeElement){
    let teamID = resetCodeElement.getAttribute("data-teamid");
    let data = {};
    data[resetCodeElement.getAttribute("data-csrf-js-name")] = resetCodeElement.getAttribute("data-csrf-js");
    data["teamID"] = teamID;
    const response = fetch('/teams/resetCode', {
        "method": "POST",
        "referrer": "http://localhost:8080/teams",
        "headers": {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        "body": new URLSearchParams(data)
    }).then((response) => {
        if(response.ok){
            populateJoinCode(document.getElementById("joinCodeDisplay"));
        }
    });
}

async function deleteTeam(deleteButtonElement){
    let teamID = deleteButtonElement.getAttribute("data-teamid");

    let data = {};
    let shouldDelete = confirm("Are you sure you want to delete: (" + deleteButtonElement.getAttribute("data-team-name") + ") this will remove all users from this team.");
    if(shouldDelete) {
        data[deleteButtonElement.getAttribute("data-csrf-js-name")] = deleteButtonElement.getAttribute("data-csrf-js");
        data["teamID"] = teamID;
        fetch('/teams/delete', {
            "method": "POST",
            "referrer": "http://localhost:8080/teams",
            "headers": {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            "body": new URLSearchParams(data)
        }).then((response) => {
            if (response.ok) {
                window.location.replace("/teams");
            }
        });
    }
}

let clipboardElement = document.getElementById("copyClipboardButton")
clipboardElement.addEventListener("click", (event) => {
    navigator.clipboard.writeText(document.getElementById("joinCodeDisplay").value);
});

let resetCodeElement = document.getElementById("refreshCodeButton")
resetCodeElement.addEventListener("click", (event) => {
    resetJoinCode(resetCodeElement);
});

let deleteTeamElement = document.getElementById("deleteTeamButton")
deleteTeamElement.addEventListener("click", (event) => {
    deleteTeam(deleteTeamElement);
});