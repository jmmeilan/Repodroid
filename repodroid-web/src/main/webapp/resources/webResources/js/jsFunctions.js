 var input;

 function setInput(datInput){
     input = datInput;
     $('div.bootstrap-tagsinput').css("width","100%");
     $('div.bootstrap-tagsinput').css("height","35px");
 }
 
 function removeInput(){
     input.remove();
 }
 
 function showSearchBox(menu){
     var value = $("select[name$='infSearch:"+menu+"'] option:selected").val();
     if (value === 'Classes'){
         $('#divClasses').removeClass('optional-search');
     }
     if (value === 'Connections'){
         $('#divConnections').removeClass('optional-search');
     }
 }

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

function AddParameter(textToAdd, FieldToFill, div){
    var value = $("select[name$='infSearch:"+textToAdd+"'] option:selected").val();
    $('#'+div+' div.bootstrap-tagsinput').append(input);
    $('#'+FieldToFill).tagsinput('add', value); 
    removeInput();
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
