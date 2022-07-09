var date = new Date();
var tdate = date.getDate();
var month = date.getMonth() + 1;
var year = date.getUTCFullYear();
if(tdate < 10){
    tdate = '0' + tdate;
}
if(month < 10) {
    month = '0' + month
}
var minDate = year + "-" + month + "-" + tdate
document.getElementById("fromDate").setAttribute('min', minDate)

var fromDate;
$('#fromDate').on('change', function() {
    fromDate = $(this).val();
    $('#toDate').prop('min', function() {
        return fromDate;
    })
});

var toDate;
$('#toDate').on('change', function() {
    toDate = $(this).val();
    $('#fromDate').prop('max', function() {
        return toDate;
    })
});