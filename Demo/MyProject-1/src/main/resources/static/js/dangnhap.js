// event register not empty input
const body = document.querySelector('body');
const btn_lg = body.querySelector('.btn-lg');
const all_inp = body.querySelectorAll('form input');
const all_lb = body.querySelectorAll('form label');
const err_inp = body.querySelectorAll('form .err');
let len = all_inp.length;
let flag_on = true;
let flag_off = false;
const evt_log = () => {
    // console.log('casasag');
    let flag = flag_on;
    for(let i = 0; i < len; i++){
        if(all_inp[i].value === ''){
            err_inp[i].innerHTML = all_lb[i].innerHTML.split(':')[0].trim() + ' là bắt buộc !';
            flag = flag_off;
        }
        else{
            err_inp[i].innerHTML = '';
        }
    }

    if(flag){
        btn_lg.type = 'submit';
    }
};

btn_lg.addEventListener('click', evt_log);

all_inp.forEach(item => {
    item.addEventListener('keyup', (e) =>{
        if(e.keyCode === 13) btn_lg.click();
    })
})

