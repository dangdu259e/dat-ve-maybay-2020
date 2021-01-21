
function change_flight_selection() {
    const return_btn = document.getElementById("Return");
    const oneway_btn = document.getElementById("oneway");
    const oneway_label = document.getElementById("oneway-label");
    const return_label = document.getElementById("return-label");
    if(return_btn.checked === true){
        return_label.style.color = "#0f62ac";

        oneway_label.style.color = "#333";

        document.getElementById("col-ahaft last").style.display="block";
    }
    if(oneway_btn.checked ===true){
        oneway_label.style.color = "#0f62ac";

        return_label.style.color = "#333";

        document.getElementById("col-ahaft last").style.display="none";
    }
}
function validate_date(){
    let today = new Date();
    const dd = String(today.getDate()).padStart(2, '0');
    const mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    const yyyy = today.getFullYear();

    const origin_date = document.getElementById("datepicker-1");
    const return_date = document.getElementById("datepicker-2");

    today = dd + '/' + mm + '/' + yyyy;

    if(origin_date === null || return_date === null){
        alert("Date is empty!!");
        return false;
    }
}
