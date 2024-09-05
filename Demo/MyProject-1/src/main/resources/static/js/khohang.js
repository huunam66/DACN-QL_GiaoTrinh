
const btn_slcat_current = (item) => {
    let class_mate = item.classList;

    if(class_mate.contains('btn-slcat')){
        class_mate.remove('btn-slcat');
        item.querySelector('input[type=checkbox]').checked = true;
        return;
    }

    class_mate.add('btn-slcat');
    item.querySelector('input[type=checkbox]').checked = false;
}

const btn_slcat = document.querySelectorAll('.btn-slcat');
btn_slcat.forEach(item => {
    if(!item.classList.contains('btn-slcat-all')){
        item.addEventListener('click', () => {btn_slcat_current(item)})
    }
})
//
//
// const btn_slcat_all = document.querySelector('.btn-slcat-all');
// const evt_btn_slcat_all = (e) => {
//
//     let class_mate = btn_slcat_all.classList;
//     let flag = false;
//     if(class_mate.contains('btn-slcat')){
//         class_mate.remove('btn-slcat');
//         btn_slcat_all.querySelector('input[type=checkbox]').checked = true;
//         flag = !flag;
//     }
//     else{
//         class_mate.add('btn-slcat');
//         btn_slcat_all.querySelector('input[type=checkbox]').checked = false;
//     }
//     // console.log(flag);
//     btn_slcat.forEach(item => {
//         if(!item.classList.contains('btn-slcat-all')){
//             let checkeding = item.querySelector('input[type=checkbox]').checked;
//             if(checkeding != flag){
//                 item.click();
//                 checkeding = flag;
//             }
//         }
//     })
// }
//
// btn_slcat_all.addEventListener('click', (e) => {evt_btn_slcat_all(e)});

const cb_checked_mod = document.querySelectorAll('.checkbox .cb-td');
const page_list = document.querySelector('.tab-ar .page-list');
let cb_temp_checked = null;
const host = 'http://localhost:8080';

const checkbox_change_evt_selected_el = async (e) => {
    let type_mod = page_list.querySelector('.type-mod');

    if(type_mod != null) page_list.removeChild(type_mod);

    let id = e.target.id.split('-')[1].trim();

    if(e.target.checked == true){
        if(!(cb_temp_checked == null) && !e.target.isSameNode(cb_temp_checked)){
            cb_temp_checked.checked = false;

            cb_temp_checked.parentElement.parentElement
                .classList.remove('checked-mod');
            cb_temp_checked.parentElement.parentElement
                .querySelector('.name')
                .querySelector('a')
                .classList.remove('cl000');
        }
        cb_temp_checked = e.target;

        e.target.parentElement.parentElement
            .classList.add('checked-mod');
        e.target.parentElement.parentElement
            .querySelector('.name')
            .querySelector('a')
            .classList.add('cl000');

        type_mod = document.createElement('div');
        type_mod.classList.add('type-mod');
        type_mod.innerHTML = `
                <a class="op-item ud" href="/book/detail?id=${id}">Xem chi tiết</a>
                <a class="op-item ud" href="/book/update?id=${id}">Sửa</a>
            `;

        let result = null;
        let request = '/load/api/roles/current/admin';
        await axios.get(host + request)
            .then(res => result = res.data.status)
            .catch(ex => result = ex);

        if(result != null && result == true){
            type_mod.innerHTML = `
                    <a class="op-item ud" href="/book/detail?id=${id}">Xem chi tiết</a>
                    <a class="op-item ud" href="/book/update?id=${id}">Sửa</a>
                    <a onclick="if(!confirm('Xác nhận xóa?')) return false" class="op-item del" href="/book/delete/${id}">Xóa</a>
                `;
        }
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
}
cb_checked_mod.forEach(item => {
    item.addEventListener('change', async (e) => {
        await checkbox_change_evt_selected_el(e);
    })
    item.addEventListener('click', (e) => {
        e.stopPropagation()
    })
    item.parentNode.parentNode.addEventListener('click', async (e) => {
        item.click();
    })
})
$("#sortDrop").change(function () {
    const value = $("#sortDrop").val();
    window.location.href = "/book?sort=" + value;
});
