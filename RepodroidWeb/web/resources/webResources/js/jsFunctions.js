
function toggle2(showHideDiv, switchTextDiv) {
    var ele = document.getElementById(showHideDiv);
    var text = document.getElementById(switchTextDiv);
    if (ele.style.display === "block") {
        ele.style.display = "none";
        text.innerHTML = "Show";
    } else {
        ele.style.display = "block";
        text.innerHTML = "Hide";
    }
}

function toggle(showHideDiv) {
    var ele = document.getElementById(showHideDiv);
    if (ele.style.display === "block") {
        ele.style.display = "none";
    } else {
        ele.style.display = "block";
    }
}

function showMsg() {
    var msg = document.getElementById("errorMsg").getElementsByTagName("li");
    var len = msg.length;
    for (var i = 0; i < len; i++) {
        if (msg[i].getAttribute("style") === "err") {
            alertify.error(msg[i].innerHTML);
        } else {
            if (msg[i].getAttribute("style") === "warn") {
                alertify.warning(msg[i].innerHTML);
            } else {
                if (msg[i].getAttribute("style") === "inf") {
                    alertify.success(msg[i].innerHTML);
                }
            }
        }
    }
}
