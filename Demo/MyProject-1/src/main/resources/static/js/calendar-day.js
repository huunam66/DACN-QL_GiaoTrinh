let ev = null;
const date_table = document.querySelector('.date-table');
date_table.addEventListener('click', e => ev = e);
const year_text = document.querySelector('.date-day .year');
const month_text = document.querySelector('.date-day .month');
const day_text = document.querySelector('.date-day .day');
const ic_calendar = document.querySelector('.date-day .bxs-calendar');
const calendar_run = () => {
    let month = new Date().getUTCMonth();
    let day = new Date().getUTCDay();

    year_text.innerText = new Date().getUTCFullYear();
    month_text.innerText = month.toString().trim().split('').length < 2 ? '0' + month.toString() : month.toString();
    day_text.innerText = day.toString().trim().split('').length < 2 ? '0' + day.toString() : day.toString();

    const years_click_event = (items) => {
        items.forEach(item => {
            item.addEventListener('click', (e) => {
                year_text.innerText = e.target.innerText;
                month_generate_and_click();
            })
        })
    }


    const day_generate_and_click = () => {
        date_table.classList.remove('year-style');
        date_table.classList.remove('month-style');
        date_table.classList.add('day-style');

        date_table.querySelector('.name-type').innerText = 'Ngày';

        year_text.classList.remove('cl-red');
        month_text.classList.remove('cl-red');
        day_text.classList.add('cl-red');
        ic_calendar.classList.add('cl-red');

        let tag = date_table.querySelector('.tag');
        tag.innerHTML = '';

        let year_int = parseInt(year_text.innerText);
        let month_int = parseInt(month_text.innerText);

        let max_day = 31;
        if(year_int % 4 === 0 && month_int === 2) max_day = 29;
        else if(month_int === 2) max_day = 28;
        else if(month_int % 2 === 0 && month_int !== 8 && month_int !== 10 && month_int !== 12)
            max_day = 30;

        for(let i = 1; i <= max_day; i++){
            let p = document.createElement('p');
            p.innerText = + i;
            if(i === parseInt(day_text.innerText))
                p.classList.add('bgcl-0088ff');

            p.addEventListener('click', (e) => {
                let day_inner = e.target.innerText;
                day_text.innerText = day_inner.split('').length < 2 ? '0' + day_inner : day_inner;
                date_table.classList.add('d-none');
            })

            tag.appendChild(p);
        }

        date_table.classList.remove('d-none');
    }

    day_text.addEventListener('click', (e) => {
        if(!date_table.classList.contains('day-style'))
            day_generate_and_click();
        else{
            ic_calendar.classList.add('cl-red');
            date_table.classList.remove('d-none');
        }
    })

    const month_generate_and_click = () => {
        date_table.classList.remove('year-style');
        date_table.classList.remove('day-style');
        date_table.classList.add('month-style');

        date_table.querySelector('.name-type').innerText = 'Tháng';

        year_text.classList.remove('cl-red');
        day_text.classList.remove('cl-red');
        month_text.classList.add('cl-red');
        ic_calendar.classList.add('cl-red');

        let tag = date_table.querySelector('.tag');
        tag.innerHTML = '';
        for(let i = 1; i <= 12; i++){
            let p = document.createElement('p');
            p.innerText = 'Tháng ' + i;
            if(i === parseInt(month_text.innerText))
                p.classList.add('bgcl-0088ff');

            p.addEventListener('click', (e) => {
                let value = e.target.innerText.split(' ')[1].trim();
                month_text.innerText = value.split('').length < 2 ? '0' + value : value;
                day_generate_and_click();
            })

            tag.appendChild(p);
        }

        date_table.classList.remove('d-none');
    }

    month_text.addEventListener('click', (e) => {
        if(!date_table.classList.contains('month-style'))
            month_generate_and_click();
        else {
            ic_calendar.classList.add('cl-red');
            date_table.classList.remove('d-none');
        }
    });


    year_text.addEventListener('click', (e) => {

        month_text.classList.remove('cl-red');
        day_text.classList.remove('cl-red');
        e.target.classList.add('cl-red');
        ic_calendar.classList.add('cl-red');

        let tag = date_table.querySelector('.tag');
        if(!date_table.classList.contains('year-style')){
            tag.innerHTML = '';
            let year_now = new Date().getFullYear();
            for(let i = year_now; i >= 1990; i--){
                let p = document.createElement('p');
                if(i === parseInt(year_text.innerText))
                    p.classList.add('bgcl-0088ff');
                p.innerText = i.toString();
                tag.appendChild(p);
            }

            date_table.classList.remove('month-style');
            date_table.classList.remove('day-style');
            date_table.classList.add('year-style');
            years_click_event(tag.querySelectorAll('p'));
            date_table.querySelector('.name-type').innerText = 'Năm';
        }
        date_table.classList.remove('d-none');
    })

    ic_calendar.addEventListener('click', (e) => {
        if(year_text.classList.contains('cl-red')) year_text.click();
        if(month_text.classList.contains('cl-red')) month_text.click();
        if(day_text.classList.contains('cl-red')) day_text.click();
        if(
            !year_text.classList.contains('cl-red')
            &&
            !month_text.classList.contains('cl-red')
            &&
            !day_text.classList.contains('cl-red')
        ){
            year_text.click();
        }
        e.target.classList.add('cl-red');
    })

    document.addEventListener('click', (e) => {
        if(e !== ev) {
            if(e.target === year_text) return;
            if(e.target === month_text) return;
            if(e.target === day_text) return;
            if(e.target === ic_calendar) return;

            date_table.classList.add('d-none');
            ic_calendar.classList.remove('cl-red');
        }
    })
}
calendar_run();
