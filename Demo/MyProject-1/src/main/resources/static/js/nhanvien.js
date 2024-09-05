// const body = document.querySelector('body');

const cb_checked_mod = body.querySelectorAll('.checkbox .cb-td');
const page_list = body.querySelector('.tab-ar .page-list');
const host = 'http://localhost:8080';
const config = {
    "headers": {
        "content-type": "application/json",
    }
}
let cb_temp_checked = null;

const checkbox_evt_change = async (e) => {
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

        let admin = null;
        let request = '/load/api/roles/current/admin';
        await axios.get(host + request)
            .then(res => admin = res.data.status)
            .catch(ex => admin = ex);

        let node_delete = '';
        let node_grant = '';
        if(admin != null && admin == true){
            node_delete = `
                    <a onclick="if(!confirm('Xác nhận xóa?')) return false" class="op-item del" href="/nhanvien/delete?id=${id}">Xóa</a>
                `;

            node_grant = `
                    <a type="button" class="op-item gr-ac">Phân quyền</a>
                `
        }

        type_mod = document.createElement('div');
        type_mod.classList.add('type-mod');
        type_mod.innerHTML = `
                <a class="op-item ud" href="/nhanvien/update?id=${id}">Sửa</a>
                ${node_delete}
                <a type="button" class="op-item cr-ac">Tạo tài khoản</a>
            `;

        let result = null;
        request = "/load/api/account/exists";
        let request_body = JSON.stringify({id: `${id}`});

        await axios.post(host + request, request_body, config)
            .then(res => result = res.data.status)
            .catch(ex => result = ex);

        if(result != null && result == true){
            type_mod.innerHTML = `
                    <a class="op-item ud" href="/nhanvien/update?id=${id}">Sửa</a>
                    ${node_delete}
                    <a type="button" class="op-item cpw-ac">Đổi mật khẩu</a>
                    ${node_grant}
                `;
        }
        // console.log(result);
        page_list.insertBefore(type_mod, page_list.firstChild);

        let type_mod_a_all = document.querySelectorAll('.type-mod a');
        type_mod_a_all.forEach(item => {
            item.addEventListener('click', (e) => {
                document.querySelector('.checked-mod input[type=checkbox]').checked = false;
            });
        })

        btn_event_mod();
    }
    else{
        e.target.parentElement.parentElement
            .classList.remove('checked-mod');
        cb_temp_checked = null;
    }
}
cb_checked_mod.forEach(item => {
    item.addEventListener('change', async (e) => {
        await checkbox_evt_change(e);
    })
    item.addEventListener('click', (e) => {
        e.stopPropagation();
    })
    item.parentNode.parentNode.addEventListener('click', async (e) => {
        item.click();
    })
})

const btn_event_mod = () => {
    let btn = body.querySelectorAll('.op-item[type=button]');
    btn.forEach(item => {
        item.addEventListener('click', async (e) => {

            let class_mate = e.target.classList;
            let mssv = cb_temp_checked.parentElement
                .parentElement.querySelector('.mssv').innerHTML;
            let ten = cb_temp_checked.parentElement
                .parentElement.querySelector('.name').innerHTML;

            if(class_mate.contains('cr-ac')){
                await create_ac(class_mate, mssv, ten);
            }
            else {
                if(class_mate.contains('cpw-ac')) {
                    await change_pw(class_mate, mssv, ten);
                }
                if(class_mate.contains('gr-ac')){
                    await grant_role(class_mate, mssv, ten);
                }
            }
        })
    })

    const grant_role = async (class_mate, mssv, ten) => {

        let box_cr_ac = document.createElement('div');
        box_cr_ac.className = 'box-cr-ac';
        let id = cb_temp_checked.id.split('-')[1].trim();
        let result = null;
        let request = "/load/api/account/username";
        let request_body = JSON.stringify({id: id});

        await axios.post(host + request, request_body, config)
            .then(res => result = res.data)
            .catch(ex => result = ex);

        let username = result.username != null ? result.username : '';

        box_cr_ac.innerHTML = `
                        ${await node(class_mate, username, mssv, ten)}
                    `;
        body.classList.add('ov-hd');
        body.appendChild(box_cr_ac);

        canc();

        let btn = body.querySelector('.box-cr-ac .subm');
        btn.addEventListener('click', async (e) => {
            if(!confirm("Xác nhận phân quyền ?")) return;

            let parent_parent = e.target.parentElement.parentElement;

            let role = parent_parent.querySelector('select[class=role]').value;

            let request = "/load/api/grant/roles";
            let request_body = JSON.stringify({id: id, new_role: role});

            let result = null;
            await axios.put(host + request, request_body, config)
                .then(res => result = res.data)
                .catch(ex => result = ex);

            mess_box(result);
        })
    }

    const create_ac = async (class_mate, mssv, ten) => {
        let box_cr_ac = document.createElement('div');
        box_cr_ac.className = 'box-cr-ac';
        box_cr_ac.innerHTML = `
                    ${ await node(class_mate, mssv, mssv, ten)}
                `;
        body.classList.add('ov-hd');
        body.appendChild(box_cr_ac);

        canc();

        let btn = body.querySelector('.subm[name="cr-ac"]');
        btn.addEventListener('click', async (e) => {
            let parent_parent = e.target.parentElement.parentElement;
            if(!not_none(parent_parent)) return;
            if(!confirm("Xác nhận tạo tài khoản ?")) return;

            let username_name = parent_parent.querySelector('.username');

            let result = null;
            let request = "/load/api/account/constraint/username";
            let request_body = JSON.stringify({username: username_name.value.trim()});
            await axios.post(host + request, request_body, config)
                .then(res => result = res.data)
                .catch(ex => result = ex);

            if(result != null && result == true){
                let parent_el = username_name.parentElement;
                let error_username = parent_el.querySelector('.error');
                let label_username = parent_el.querySelector('label');
                error_username.innerHTML = label_username.innerHTML.split(':')[0].trim();
                return;
            }

            let password = parent_parent.querySelector('.password');
            let id = cb_temp_checked.id.split('-')[1].trim();

            request = '/load/api/account/create';
            request_body = JSON.stringify({
                id: id,
                username: username_name.value.trim(),
                password: password.value.trim()
            });
            await axios.post(host + request, request_body, config)
                .then(res => result = res.data)
                .catch(ex => result = ex);

            mess_box(result);
        })
    }


    const change_pw = async (class_mate, mssv, ten) => {
        let box_cr_ac = document.createElement('div');
        box_cr_ac.className = 'box-cr-ac';
        let id = cb_temp_checked.id.split('-')[1].trim();
        let result = null;
        let request = "/load/api/account/username";
        let request_body = JSON.stringify({id: id});

        await axios.post(host + request, request_body, config)
            .then(res => result = res.data)
            .catch(ex => result = ex);

        let username = result.username != null ? result.username : '';
        box_cr_ac.innerHTML = `
                        ${await node(class_mate, username, mssv, ten)}
                    `;
        body.classList.add('ov-hd');
        body.appendChild(box_cr_ac);

        canc();

        let btn_ch_pw = body.querySelector('.subm[name="ch-pw"]');
        btn_ch_pw.addEventListener('click', async (e) => {
            if(!not_none(e.target.parentElement.parentElement)) return;
            if(!confirm("Xác nhận đổi mật khẩu ?")) return;
            let password = e.target.parentElement
                .parentElement.querySelector('input[type=password]').value;

            let request = "/load/api/account/change/password";
            let request_body = JSON.stringify({id: id, pw: password});
            await axios.put(host + request, request_body, config)
                .then(res => result = res.data)
                .catch(ex => result = ex);

            mess_box(result);
        })
    }

    const canc = () => {
        let btn_canc = body.querySelector('.box-cr-ac .canc');
        btn_canc.addEventListener('click', () => {
            body.classList.remove('ov-hd');
            box_cr_ac = body.querySelector('.box-cr-ac');
            body.removeChild(box_cr_ac);
        })
    }

    const not_none = (box) => {
        let flag = true;
        const inp = box.querySelectorAll('input');
        inp.forEach(item => {
            console.log(item);
            let parent = item.parentElement;
            let error = parent.querySelector('.error');
            if(item.value == ''){
                flag = false;
                let label = parent
                    .querySelector('label').innerHTML.split(':')[0].trim();
                error.innerHTML = label + ' là bắt buộc !';
            }
            else{
                if(error != null) error.innerHTML = '';
            }
        })
        return flag;
    }

    const mess_box = (result) => {
        let box_cr_ac = body.querySelector('.box-cr-ac');
        body.removeChild(box_cr_ac);
        let mess_site = document.createElement('div');
        mess_site.className = 'mess-site';
        mess_site.innerHTML = `
                            <div class="mess-box">
                                <p class="mess-text" style="color: ${result.color}">${result.status}</p>
                                <img type="hidden" style="display: none;" src="${result.static}">
                                <img class="ic-ss" src="${result.gif}">
                            </div>
                        `;
        body.appendChild(mess_site);
        setTimeout(() => {
            $('.mess-box img')
                .attr('src', () => {
                    return $('.mess-box img[type=hidden]').attr('src');
                })
            $('.mess-box img[type=hidden]').remove();
            setTimeout(() => {
                $('.mess-site').remove();
                body.classList.remove('ov-hd');
            }, 1500)
        }, 1200);
    }


    let node = async (class_mate, username, mssv, ten) => {
        let cus = null;
        if(class_mate.contains('cr-ac')){
            cus = `
                <div class="box-main">
                    <p class="row-b">
                        <label>Tên sinh viên:</label>
                        <input type="text" class="name" value="${ten} - ${mssv}" readonly>
                    </p>
                    <p class="row-b">
                        <label class="required">Tên tài khoản:</label>
                        <input disabled type="text" class="username" value="${username}">
                        <span class="error"></span>
                    </p>
                    <p class="row-b cus">
                        <label class="required">Mật khẩu:</label>
                        <input type="password" class="password">
                        <span class="error"></span>
                    </p>
                    <p class="row-sm">
                        <button class="sm canc">Hủy bỏ</button>
                        <button class="sm subm" name="cr-ac">Tạo tài khoản</button>
                    </p>
                </div>
            `;
        }
        else if(class_mate.contains('cpw-ac')){
            cus = `
                <div class="box-main">
<!--                    <button class="del-ac">Gỡ tài khoản</button>-->
                    <p class="row-b">
                        <label>Tên sinh viên:</label>
                        <input type="text" class="name" value="${ten} - ${mssv}" readonly>
                    </p>
                    <p class="row-b">
                        <label>Tên tài khoản:</label>
                        <input type="text" class="username" value="${username}" readonly>
                        <span class="error"></span>
                    </p>
                    <p class="row-b cus">
                        <label class="required">Mật khẩu mới:</label>
                        <input type="password" class="password">
                        <span class="error"></span>
                    </p>
                    <p class="row-sm">
                        <button class="sm canc">Hủy bỏ</button>
                        <button class="sm subm" name="ch-pw">Đổi mật khẩu</button>
                    </p>
                </div>
            `;
        }
        else{
            if(class_mate.contains('gr-ac')){
                let manager = null;
                let id = cb_temp_checked.id.split('-')[1].trim();
                let request = '/load/api/roles/is/manager';
                let request_body = JSON.stringify({id: id});
                await axios.post(host + request, request_body, config)
                    .then(res => manager = res.data.status)
                    .catch(ex => manager = ex);
                let user_selected = manager == null || manager != null && manager != true ? 'selected' : '';
                let manager_selected = manager != null && manager == true ? 'selected' : '';

                cus = `
                    <div class="box-main">
                        <p class="row-b">
                            <label>Tên sinh viên:</label>
                            <input type="text" class="name" value="${ten} - ${mssv}" readonly>
                        </p>
                        <p class="row-b">
                            <label>Tên tài khoản:</label>
                            <input type="text" class="username" value="${username}" readonly>
                            <span class="error"></span>
                        </p>
                        <p class="row-b cus">
                            <label class="required">Vài trò (quyền hạn):</label>
                            <select class="role">
                                <option value="USER" ${user_selected}>USER</option>
                                <option value="MANAGER" ${manager_selected}>MANAGER</option>
                            </select>
                        </p>
                        <p class="row-sm">
                            <button class="sm canc">Hủy bỏ</button>
                            <button class="sm subm">Phân quyền</button>
                        </p>
                    </div>
                `;
            }
        }
        return cus;
    }
}


const typing_select_childs = document.querySelectorAll('.middle select');
typing_select_childs.forEach(item => {
    item.addEventListener('change', (e) => {
        document.querySelector('.ele-1 button[type=submit]').click();
    })
})