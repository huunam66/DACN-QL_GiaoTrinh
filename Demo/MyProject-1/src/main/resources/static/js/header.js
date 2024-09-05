//const body = document.querySelector('body');
const right_inf = body.querySelector('.right-inf');
const right_inf_button = right_inf.querySelector('.right-inf button');
const right_inf_button_evt = () => {
    if(right_inf.classList.contains('openul')){
        right_inf.classList.remove('openul');
        return;
    }
    
    right_inf.classList.add('openul');
}
right_inf_button.addEventListener('click', right_inf_button_evt);


let current_filter_book = null;
const evt_filter_book = (item) => {
    if(current_filter_book != item && current_filter_book != null){
        current_filter_book.classList.remove('btn-btn-select');
    }

    let class_mate = item.classList;
    if(class_mate.contains('btn-btn-select')){
        class_mate.remove('btn-btn-select');
        return;
    }

    class_mate.add('btn-btn-select');
    current_filter_book = item;
}
const filter_book = body.querySelectorAll('.ele-2 .cat .btn-btn');
filter_book.forEach(item => item.addEventListener('click', () => {evt_filter_book(item)}))
