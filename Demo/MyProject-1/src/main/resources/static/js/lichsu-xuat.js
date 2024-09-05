//const body = document.querySelector('body');

//---------------------------------------
// event for caret-down
//---------------------------------------

const submit_cr = body.querySelector('.submit-cr');
const more = submit_cr.querySelector('.more');
const caret_down_evt = () => {
    let class_mate = more.classList;
    if(class_mate.contains('more-show')){
        class_mate.remove('more-show');
        return;
    }

    class_mate.add('more-show');
}

const caret_down = body.querySelector('.submit-cr .caret-down');
caret_down.addEventListener('click', caret_down_evt);

const cb_checked_mod = body.querySelectorAll('.checkbox .cb-td');
const page_list = body.querySelector('.tab-ar .page-list');
let cb_temp_checked = null;
cb_checked_mod.forEach(item => {
    item.addEventListener('change', (e) => {
        let type_mod = page_list.querySelector('.type-mod');

        if(type_mod != null) page_list.removeChild(type_mod);

        let id = e.target.id.split('-')[1].trim();

        if(e.target.checked == true){
            if(!(cb_temp_checked == null) && !e.target.isSameNode(cb_temp_checked)){
                cb_temp_checked.checked = false;

                cb_temp_checked.parentElement.parentElement
                    .classList.remove('checked-mod');
            }
            cb_temp_checked = e.target;

            e.target.parentElement.parentElement
                .classList.add('checked-mod');

            type_mod = document.createElement('div');
            type_mod.classList.add('type-mod');
            type_mod.innerHTML = `
                <a class="op-item ud" href="/lichsu/xuat/details/${id}">Xem chi tiết</a>
            `;

            // console.log((result))
            page_list.insertBefore(type_mod, page_list.firstChild);

            let type_mod_a_all = document.querySelectorAll('.type-mod a');
            type_mod_a_all.forEach(item => {
                item.addEventListener('click', (e) => {
                    document.querySelector('.checked-mod input[type=checkbox]').checked = false;
                });
            })
        }
        else{
            e.target.parentElement.parentElement
                .classList.remove('checked-mod');
            e.target.parentElement.parentElement
                .querySelector('.name')
                .querySelector('a')
                .classList.remove('cl000');
        }
    })
})

$("#sortDrop").change(function () {
    const value = $("#sortDrop").val();
    window.location.href = "/lichsu/xuat?sort=" + value;
});
