
const host = 'http://localhost:8080';
const config = {
    "headers": {
        "content-type": "application/json"
    }
}

let giaotrinh_temp_array = [];

const inp_gt_ev_click = async (e) => {
    if(e.target.value === ''){
        let list_search = e.target.parentNode.querySelector('.list-search ul');
        if(list_search.children.length === 0){
            await call_and_flow_gt(e);

        }
    }
}

const call_and_flow_gt = async (e) => {
    let request = `/load/api/book/search?name=${e.target.value}`;
    await flow_data(e, request, 1===2);
}

const tab_book = body.querySelector('.tab-book tbody');
const soLuong_bt = body.querySelector('.other-r .soluong .num');
const tongtien_bt = body.querySelector('.other-r .tongtien .num');
const tiencantra_bt = body.querySelector('.other-r .tiencantra .num');
const mem_child_gt_click_xuat = (e) => {
    let figure = e.target.parentNode.parentNode;
    let tonkho_tag = figure.querySelector('p[title="Tồn kho"]');
    let tonkho = parseInt(tonkho_tag.innerText);
    let ten = figure.querySelector('p[title="Tên giáo trình"]').innerText;

    if(tonkho === 0){
        alert('Không đủ số lượng, hãy nhập thêm !');
        return;
    }
    if(check_exitst_book(ten)) {
        alert('Giáo trình này đã được chọn !');
        return;
    }
    let name = figure.querySelector('.name').innerText.trim();
    let anh = figure.querySelector('.pic-ph').querySelector('img').getAttribute('src');
    let gia_p = figure.querySelector('p[title="Giá"]');
    let gia = gia_p.getAttribute("data-price");
    let gia_text = gia_p.innerText;
    let id = figure.querySelector('button[title="Chọn"]').getAttribute('data_id');
    let max_size = figure.getAttribute('max-size');

    let tr = document.createElement('tr');
    tr.innerHTML = `
        <td class="stt"></td>
        <td class="anh">
            <img src="${anh}">
        </td>
        <td class="ten">
            <input type="hidden" name="giaotrinh_id" value="${id}">
            <p>${name}</p>
        </td>
        <td class="slxuat">
            <input type="number" value="1" min="1" prev-val="1" max="${max_size}" name="soluong">
        </td>
        <td class="dongia">
            <input disabled type="number" value="${gia}" min="1" max="99999999" name="dongia">
        </td>
        <td class="thanhtien">${gia_text}</td>
        <td class="del" title="Loại bỏ">
            <i class="ph ph-trash"></i>
        </td>
    `;

    tab_book.appendChild(tr);
    list_search_close.click();
    let tonkho_el = e.target.parentNode.parentNode.querySelector('[title="Tồn kho"]');
    let soluong_ton = parseInt(tonkho_el.innerText);
    tonkho_el.innerText = soluong_ton - 1;
    count_change(tab_book.lastChild);
    price_change(tab_book.lastChild);
    count_keyup(tab_book.lastChild);
    price_keyup(tab_book.lastChild);
    del_item_ic_click(tab_book.lastChild);
    update_stt_sl_price();
}


const flow_data = async (e, request, flag) => {
    const list_tag = e.target.parentNode.querySelector('.list-search ul');

    let result = null;
    await axios.get(host + request)
        .then(res => result = res)
        .catch(ex => result = ex);

    if(result != null && result.data != null){
        result.data.forEach(item => {
            let li = document.createElement('li');
            li.title = item.ten;
            li.innerHTML = flag ? flow_lop(item) : flow_gt(item);
            list_tag.appendChild(li);
        })

        if(!flag){
            giaotrinh_temp_array = list_tag.querySelectorAll('li');
        }

        list_tag.querySelectorAll('.btn-choose')
            .forEach(item => {
                item.addEventListener('click', (e) => {
                    if(flag) mem_child_lop_click(e);
                    else mem_child_gt_click_xuat(e);
                })
            })

        let lit_search = list_tag.parentNode;
        let childs = lit_search.querySelectorAll('*');
        childs.forEach(i => {
            i.addEventListener('click', (e) => {
                e.stopPropagation();
            })
        })
    }
}


const flow_gt = (data) => {
    let soluong_input = data.soluong;
    let tab_book_tr = document.querySelector('.tab-book tbody');
    if(tab_book_tr.childNodes.length > 0){
        tab_book_tr = tab_book_tr.querySelectorAll('tr');
        for(let i = 0; i < tab_book_tr.length; i++){
            let id_giaotrinh_tab = tab_book_tr[i].querySelector('[name=giaotrinh_id]').value;
            id_giaotrinh_tab = parseInt(id_giaotrinh_tab);
            id_giaotrinh_data = parseInt(data.id);
            if(id_giaotrinh_tab == id_giaotrinh_data){
                let soluong_giaotrinh_tr = tab_book_tr[i].querySelector('[name=soluong]').value;
                soluong_input -= parseInt(soluong_giaotrinh_tr);
            }
        }
    }
    const cus = `
      <figure max-size="${data.soluong}">
        <p title="Ảnh" class="pic-ph">
            <img src="/pictures/book/${data.hinhanh}">
        </p>
        <p class="pic-ph">
            <button title="Chọn" data_id="${data.id}" class="btn-choose">Chọn</button>
        </p>
        <p title="Tên giáo trình" class="name">${data.ten}</p>
        <p title="Giá" class="phone" data-price="${data.gia}">${data.gia.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</p>
        <p title="Tồn kho" class="phone" max-size="${data.soluong}">${soluong_input}</p>
        <p title="Thuộc chuyên ngành" class="email">${data.chuyennganh.tenchuyennganh}</p>
      </figure>
    `;
    return cus;
}

const flow_lop = (data) => {
    const cus = `
      <figure>
        <p class="pic-ph">
            <button title="Chọn" data-set="${data.id}" class="btn-choose">Chọn</button>
        </p>
        <p title="Tên lớp" class="phone" name="tenlop" >${data.tenlop}</p>
        <p title="Môn học" class="name" name="monhoc">${data.monhoc}</p>
        <p title="Giảng viên" class="phone" name="giangvien">${data.giangvien}</p>
        <p title="Phòng học" class="phone" name="phonghoc">${data.phonghoc}</p>
        <p title="Thời gian học" class="phone" name="thoigianhoc">${data.thoigianhoc}</p>
        <p title="Năm học" class="phone" name="namhoc">${data.namhoc}</p>
      </figure>
    `;
    return cus;
}

const lop_inf = body.querySelector('.lop-inf');
const mem_child_lop_click = (e) => {
    let figure = e.target.parentNode.parentNode;
    let tenlop = figure.querySelector('[name=tenlop]').innerText;
    let monhoc = figure.querySelector('[name=monhoc]').innerText;
    let giangvien = figure.querySelector('[name=giangvien]').innerText;
    let phonghoc = figure.querySelector('[name=phonghoc]').innerText;
    let thoigianhoc = figure.querySelector('[name=thoigianhoc]').innerText;
    let namhoc = figure.querySelector('[name=namhoc]').innerText;
    let id = figure.querySelector('button[title="Chọn"]').getAttribute('data-set');

    lop_inf.querySelector('[name=lop_id]').value = id;
    lop_inf.querySelector('[name=tenlop]').value = tenlop;
    lop_inf.querySelector('[name=monhoc]').value = monhoc;
    lop_inf.querySelector('[name=giangvien]').value = giangvien;
    lop_inf.querySelector('[name=phonghoc]').value = phonghoc;
    lop_inf.querySelector('[name=thoigian]').value = thoigianhoc;
    lop_inf.querySelector('[name=namhoc]').value = namhoc;

    list_search_close.click();

}

const count_change_and_update_tonkho = (e) => {
    let id = e.target.parentNode.parentNode.querySelector('[name=giaotrinh_id]').value;
    id = parseInt(id);
    let prev_val = e.target.getAttribute('prev-val');
    for(let i = 0; i < giaotrinh_temp_array.length; i++){
        let giaotrinh_id_temp_el = parseInt(giaotrinh_temp_array[i].querySelector('.btn-choose').getAttribute('data_id'));

        if(id == giaotrinh_id_temp_el){
            let tonkho = giaotrinh_temp_array[i].querySelector('[title="Tồn kho"]').innerText;
            tonkho = parseInt(tonkho);
            let val_change_current = parseInt(e.target.value);

            let item_tr_gt_seach = document.querySelector('.layout-main-site .list-search ul')
                .querySelector(`.btn-choose[data_id="${id}"]`);

            let item_ton_kho = item_tr_gt_seach != null
                ? item_tr_gt_seach.parentNode.parentNode.querySelector('[title="Tồn kho"]')
                : null;

            let max_size_giaotrinh = item_ton_kho.getAttribute('max-size');
            max_size_giaotrinh = parseInt(max_size_giaotrinh);

            if(prev_val - val_change_current < 0){
                tonkho -= Math.abs(prev_val - val_change_current);
            }
            if(prev_val - val_change_current > 0){
                tonkho += (prev_val - val_change_current);
            }

            if(tonkho <= 0 && val_change_current > max_size_giaotrinh){
                alert('Vượt quá số lượng cho phép !');
                e.target.value = e.target.getAttribute('max');
                giaotrinh_temp_array[i].querySelector('[title="Tồn kho"]').innerText = 0;
                if(item_ton_kho != null) item_ton_kho.innerText = 0;
            }
            else{
                if(prev_val - val_change_current < 0){
                    giaotrinh_temp_array[i].querySelector('[title="Tồn kho"]').innerText = tonkho;
                    if(item_ton_kho != null) item_ton_kho.innerText = tonkho;
                }
                if(prev_val - val_change_current > 0){
                    giaotrinh_temp_array[i].querySelector('[title="Tồn kho"]').innerText = tonkho;
                    if(item_ton_kho != null) item_ton_kho.innerText = tonkho;
                }
            }
        }
    }
}

const count_change = (tr) => {
    let num = tr.querySelector('.slxuat input');
    num.addEventListener('change', (e) => {
        if(e.target.value < 1) e.target.value = 1;
        count_change_and_update_tonkho(e);
        e.target.setAttribute('prev-val', e.target.value);
        update_stt_sl_price();
        thanhtien_change(tr);
    })
}

const price_change = (tr) => {
    let price = tr.querySelector('.dongia input');
    price.addEventListener('change', (e) => {
        update_stt_sl_price();
        thanhtien_change(tr);
    })
}

const count_keyup = (tr) => {
    let num = tr.querySelector('.slxuat input');
    num.addEventListener('keyup', (e) => {
        if(e.target.value == '') return;
        count_change_and_update_tonkho(e);
        e.target.setAttribute('prev-val', e.target.value);
        update_stt_sl_price();
        thanhtien_change(tr);
    })
}

const price_keyup = (tr) => {
    let price = tr.querySelector('.dongia input');
    price.addEventListener('keyup', (e) => {
        if(e.target.value < 1) e.target.value = 1;
        update_stt_sl_price();
        thanhtien_change(tr);
    })
}

const thanhtien_change = (tr) => {
    let num = tr.querySelector('.slxuat input').value;
    let price = tr.querySelector('.dongia input').value;
    let thanhtien_td = tr.querySelector('.thanhtien');
    let thanhtien = parseInt(num) * parseInt(price);
    thanhtien_td.innerText = thanhtien.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
}

const del_item_ic_click = (tr) => {
    const del_ic = tr.querySelector('.del i');
    del_ic.addEventListener('click', (e) => {

        if(!confirm('Loại bỏ ?')) return;

        tab_book.removeChild(e.target.parentNode.parentNode);
        let id = e.target.parentNode.parentNode.querySelector('[name=giaotrinh_id]').value;
        for(let i = 0; i < giaotrinh_temp_array.length; i++){
            let id_giaotrinh_temp = parseInt(giaotrinh_temp_array[i].querySelector('.btn-choose').getAttribute('data_id'));
            if(id_giaotrinh_temp == id){

                let tonkho = giaotrinh_temp_array[i].querySelector('[title="Tồn kho"]').innerText;
                tonkho = parseInt(tonkho) + parseInt(e.target.parentNode.parentNode.querySelector('[name="soluong"]').value);

                giaotrinh_temp_array[i].querySelector('[title="Tồn kho"]').innerText = tonkho;
                let item_tr_gt_seach = document.querySelector('.layout-main-site .list-search ul')
                    .querySelector(`.btn-choose[data_id="${id}"]`);

                let item_ton_kho = item_tr_gt_seach != null
                    ? item_tr_gt_seach.parentNode.parentNode.querySelector('[title="Tồn kho"]')
                    : null;

                if(item_ton_kho != null) item_ton_kho.innerText = tonkho;
                break;
            }
        }
        update_stt_sl_price();
        thanhtien_change(tr);
    })
}

const update_stt_sl_price = () => {
    const tr = tab_book.querySelectorAll('tr');
    let len = tr.length;
    let sl = 0;
    let thanhtien = 0;
    for(let i = 0; i < len; i++){
        let stt = tr[i].querySelector('.stt');
        stt.innerText = i + 1;
        let soluong_s = tr[i].querySelector('.slxuat input').value;
        sl += parseInt(soluong_s);
        let dongia_s = tr[i].querySelector('.dongia input').value;
        thanhtien += (parseInt(dongia_s) * soluong_s);
    }

    soLuong_bt.innerText = sl;
    let tongtien = thanhtien.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    tongtien_bt.innerText = tongtien;
    tiencantra_bt.innerText = tongtien;
}

const check_exitst_book = (name) => {
    let tr_s = tab_book.querySelectorAll('tr');
    let len = tr_s.length;
    if(len > 0){
        for(let i = 0; i < len; i++){
            let ten = tr_s[i].querySelector('td[class=ten]').querySelector('p').innerText;
            if(ten === name){
                return true;
            }
        }
    }
    return false;
}



inp_gt.addEventListener('click', async (e) => {
    await inp_gt_ev_click(e);
    inp_ev(e);
})

const inp_gt_keyup_ev = async (e) => {
    const list_gt = e.target.parentNode.querySelector('.list-search ul');
    list_gt.innerHTML = ``;
    await call_and_flow_gt(e)
}
inp_gt.addEventListener('keyup',  async (e) => await inp_gt_keyup_ev(e));



const input_all = body.querySelectorAll('p input[title="Bắt buộc"]');

const btn_sm = body.querySelector('.submit-cr button');
const btn_sm_submit = async (e) => {
    let len = input_all.length;
    for(let i = 0; i < len; i++){
        if(input_all[i].value === ''){
            alert('Nhập các thông tin bắt buộc !');
            return;
        }
    }

    if(tab_book.children.length < 1){
        alert('Chọn sách muốn xuất !');
        return;
    }

    if(!confirm('Xác nhận lập phiếu ?')) return;

    let ghichu = body.querySelector('textarea[class=note]').value;
    let tr = tab_book.querySelectorAll('tr');
    let request_body = {
        ghichu: ghichu,
        tennguoinhan: body.querySelector('input[name=tennguoinhan]').value,
        sdtnguoinhan: body.querySelector('input[name=sdtnguoinhan]').value,
        lop_id: document.querySelector('[name=lop_id]').value,
        chitietphieuxuats: []
    }

    tr.forEach(item => {
        request_body.chitietphieuxuats.push({
            soluong: item.querySelector('td input[name=soluong]').value,
            dongia: item.querySelector('td input[name=dongia]').value,
            giaotrinh_id: item.querySelector('td input[name=giaotrinh_id]').value
        });
    })

    console.log(request_body);
    loading();
    let result = null;
    let request = '/load/api/phieuxuat/create';
    await axios.post(host + request, request_body, config)
        .then(res => result = res)
        .catch(ex => result = ex);


    mess_box(result.data);
}
btn_sm.addEventListener('click', async (e) => await btn_sm_submit(e));

const mess_box = (data) => {
    let loading_mess = body.querySelector('.mess-site');
    body.removeChild(loading_mess);
    $('body').addClass('ov-hd');
    const mess_box = $('.mess-box');
    if (mess_box.length === 0) {
        // console.log('asf');
        $('<div>', {
            class: 'mess-site',
            html: `
                <div class="mess-box">
                    <p class="mess-text" style="${'color:' + data.color}">${data.status}</p>
                    <img type="hidden" style="display: none;" src="${data.static}">
                    <img class="ic-ss" src="${data.gif}">
                    <p class="btn-cls">
                        <a href="/">Hoàn tất</a>
                        <button>Tiếp tục</button>
                    </p>
                </div>
            `
        }).appendTo('body');

        setTimeout(() => {
            $('.mess-box img')
                .attr('src', () => {
                    return $('.mess-box img[type=hidden]').attr('src');
                })
            $('.mess-box img[type=hidden]').remove();
        }, 1200);

        $('.mess-box .btn-cls button')
            .on('click', function () {
                $(this).closest('.mess-site').remove();
                $('body').removeClass('ov-hd');
            })
    }
}


const call_and_flow_lop = async (e) => {
    let request = `/load/api/lop/search?text=${e.target.value}`;
    await flow_data(e, request, 1===1);
}

const inp_lop_ev_click = async (e) => {
    if(e.target.value === ''){
        let list_search = e.target.parentNode.querySelector('.list-search ul');
        if(list_search.children.length === 0){
            await call_and_flow_lop(e);
        }
    }
}

inp_ncc.addEventListener('click', async (e) => {
    await inp_lop_ev_click(e);
    inp_ev(e);
})

const inp_ncc_keyup_ev = async (e) => {
    const list_ncc = e.target.parentNode.querySelector('.list-search ul');
    list_ncc.innerHTML = ``;
    await call_and_flow_lop(e);
}

inp_ncc.addEventListener('keyup', async (e) => await inp_ncc_keyup_ev(e));


const loading = () => {
    let mess_site = document.createElement('div');
    mess_site.className = 'mess-site';
    mess_site.innerHTML = `<img class="loadding" src="/pictures/main/pngtree-loading-icon-vector-transparent-png-image_5687537.png">`;
    body.appendChild(mess_site);
}

document.querySelector('#SDT').addEventListener('keydown', function (e) {

    if (e.keyCode != 8 && (e.keyCode < 48 || e.keyCode > 57)) {
        e.preventDefault();
    }

})