var today = new Date();
var dd = today.getDate();
var mm = today.getMonth() + 1; // January is 0
var yyyy = today.getFullYear();
var ckObj = {};
var ball;
var deptsObj = [];

if (dd < 10) {
	dd = '0' + dd;
}

if (mm < 10) {
	mm = '0' + mm;
}

today = yyyy + '-' + mm + '-' + dd;

window.onload = function () {
    var ck = decodeURIComponent(document.cookie);
    var cks = ck.split(';');
    for (var i = 0; i < cks.length; i++) {
        var tempCk = cks[i].split('=');
        tempCk[0] = tempCk[0].trim();
        tempCk[1] = tempCk[1].trim();
        ckObj[tempCk[0]] = tempCk[1];
        console.log(tempCk[0]);
        console.log(ckObj[tempCk[0]]);
    }
    var data = 0;
    document.getElementById("cost").addEventListener("change", alerter);
    document.getElementById("typeOfEvent").addEventListener("change", alerter);
    handleOptions(data);
//document.getElementById("formSubmit").addEventListener("click", postForm,
//		false);
//document.getElementById("currentDate").value = today;
};



function inputDepts(depts) {
    var allIds = Object.keys(depts);
    var deptName = document.getElementById("deptId");
    var opt = document.createElement("option");
    opt.value = 0;
    opt.selected = "selected";
    opt.text = "Select Department";
    deptName.appendChild(opt);
    for (var i = 0; i < allIds.length; i++) {
        var opt = document.createElement("option");
        opt.value = allIds[i];
        opt.text = depts[opt.value];
        console.log(opt.value);
        console.log(opt.text);
        deptName.appendChild(opt);
    }

}

function handleOptions(data){
        var xhr = new XMLHttpRequest();
        var depts = '';
        xhr.onreadystatechange = function () {
            console.log(" " + xhr.readyState + xhr.status);
            if (xhr.readyState == 4 && xhr.status == 200) {
                console.log(xhr.responseText);
                depts = JSON.parse(xhr.responseText);

                inputDepts(depts);
            }
        }
        xhr.open("GET", "http://localhost:8082/Project1-Copy//getdepartments", true);
        xhr.send();
};

function alerter() {
	document.getElementById("cost").value = parseFloat(
			document.getElementById("cost").value).toFixed(2);
	var amount = document.getElementById("cost").value;

	var event = document.getElementById("typeOfEvent").value;
	var factor = 0;
	switch (event) {
	case "1":
		factor = 0.80;
		//document.getElementById("reimbamtEst").value = parseFloat(
		//		(amount )).toFixed(2);
		;
		break;
	case "2":
		factor = 0.60;
		//document.getElementById("reimbamtEst").value = parseFloat(
		//		(amount * .60)).toFixed(2);
		break;
	case "3":
		factor = 0.75;
		//document.getElementById("reimbamtEst").value = parseFloat(
		//		(amount * .80)).toFixed(2);
		break;
	case "4":
		factor =1.00;
		//document.getElementById("reimbamtEst").value = parseFloat(
		//		amount * .75)
		//		.toFixed(2);
		break;
	case "5":
		factor = 0.90;
		//document.getElementById("reimbamtEst").value = parseFloat(
		//		(amount * .90)).toFixed(2);
		break;
	case "6":
		factor = 0.30;
		//document.getElementById("reimbamtEst").value = parseFloat(
		//		(amount * .30)).toFixed(2);
		break;
	default:
		console.log("Reimbursement Amount Error");
	}
	document.getElementById("reimbursementamount").value = parseFloat(
			(amount * factor)).toFixed(2);
	    ball = 1000;//ckObj["remainingReimbursementAmount"].toString();

        document.getElementById("cost").max = (ball / factor);

}