//функция вызова таймера

function setData(timer, id) {

    // timer= timer+1;
    console.log(timer);
    if (timer < 0) {
        return false;
    }

    var hour = parseInt(timer / (60 * 60)) % 24;
    if (hour < 10) {
        hour = '0' + hour;
    }
    hour = hour.toString();

    var min = parseInt(timer / (60)) % 60;
    if (min < 10) {
        min = '0' + min;
    }
    min = min.toString();

    var sec = parseInt(timer) % 60;
    if (sec < 10) {
        sec = '0' + sec;
    }
    sec = sec.toString();

    var time = hour + ":" + min + ":" + sec;

    document.getElementById(id).innerHTML = time;
    // setTimeout(function () {
    //     setData(timer)
    // }, 1000);
}

