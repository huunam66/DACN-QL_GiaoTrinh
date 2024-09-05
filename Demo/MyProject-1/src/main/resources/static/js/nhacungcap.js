// const body = document.querySelector('body');
const cb_checked_mod = body.querySelectorAll('.checkbox .cb-td');
const page_list = body.querySelector('.tab-ar .page-list');
let cb_temp_checked = null;
const host = 'http://localhost:8080';
cb_checked_mod.forEach(item => {
    item.addEventListener('change', async (e) => {
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
                <a class="op-item ud" href="/nhacungcap/update?id=${id}">Sửa</a>
            `;

            let result = null;
            let request = '/load/api/roles/current/admin';
            await axios.get(host + request)
                .then(res => result = res.data.status)
                .catch(ex => result = ex);

            if(result != null && result == true){
                type_mod.innerHTML = `
                    <a class="op-item ud" href="/nhacungcap/update?id=${id}">Sửa</a>
                    <a onclick="if(!confirm('Xác nhận xóa?')) return false" class="op-item del" href="/nhacungcap/delete?id=${id}">Xóa</a>
                `;
            }

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
        }
    })
})