

const host = 'http://localhost:8080';
const config = {
    "headers": {
        "content-type": "application/json"
    }
}

const inp_ncc_ev_click = async (e) => {
    if(e.target.value === ''){
        let list_search = e.target.parentNode.querySelector('.list-search ul');
        if(list_search.children.length === 0){
            await call_and_flow_ncc(e);
        }
    }
}

const inp_gt_ev_click = async (e) => {
    if(e.target.value === ''){
        let list_search = e.target.parentNode.querySelector('.list-search ul');
        if(list_search.children.length === 0){
            await call_and_flow_gt(e);
        }
    }
}

const call_and_flow_ncc = async (e) => {
    let request = `/load/api/phieunhap/nhacungcap/search?name=${e.target.value}`;
    await flow_data(e, request, 1===1);
}
const call_and_flow_gt = async (e) => {
    let request = `/load/api/book/search?name=${e.target.value}`;
    await flow_data(e, request, 1===2);
}


const nhacungcap_inf = body.querySelector('.nhacungcap-inf');
const mem_child_ncc_click = (e) => {
    let figure = e.target.parentNode.parentNode;
    let name_tag = figure.querySelector('.name');
    let name = name_tag.innerText;
    let diachi = name_tag.getAttribute('data-address');
    let phone = figure.querySelector('.phone').innerText;
    let email = figure.querySelector('.email').innerText;
    let id = figure.querySelector('button[title="Chọn"]').getAttribute('data-set');

    nhacungcap_inf.querySelector('.name')
        .querySelector('input[readonly]').value = name;
    nhacungcap_inf.querySelector('.phone')
        .querySelector('input[readonly]').value = phone;
    nhacungcap_inf.querySelector('.email')
        .querySelector('input[readonly]').value = email;
    nhacungcap_inf.querySelector('.address')
        .querySelector('input[readonly]').value = diachi;
    nhacungcap_inf.querySelector('input[name=nhacungcap_id]').value = id;

    list_search_close.click();

}

const tab_book = body.querySelector('.tab-book tbody');
const soLuong_bt = body.querySelector('.other-r .soluong .num');
const tongtien_bt = body.querySelector('.other-r .tongtien .num');
const tiencantra_bt = body.querySelector('.other-r .tiencantra .num');
const mem_child_gt_click = (e) => {
    let figure = e.target.parentNode.parentNode;
    let ten = figure.querySelector('p[title="Tên giáo trình"]').innerText;

    if(check_exitst_book(ten)) {
        alert('Giáo trình này đã được chọn !');
        return;
    }

    let anh = figure.querySelector('p[title="Ảnh"]').querySelector('img').getAttribute('src');
    let gia_p = figure.querySelector('p[title="Giá"]');
    let gia = gia_p.getAttribute("data-price");
    let gia_text = gia_p.innerText;
    let id = figure.querySelector('p button[title="Chọn"]').getAttribute('data-set');
    let tr = document.createElement('tr');
    tr.innerHTML = `
            <td class="stt">
            </td>
            <td class="anh">
                <img src="${anh}">
            </td>
            <td class="ten">
                <input type="hidden" name="giaotrinh_id" value="${id}">
                <p>${ten}</p>
            </td>
            <td class="slnhap">
                <input type="number" value="1" min="1" max="500" name="soluong">
            </td>
            <td class="dongia">
                <input type="number" value="${gia}" min="1" max="9999999" name="dongia">
            </td>
            <td class="thanhtien">${gia_text}</td>
            <td class="del" title="Loại bỏ">
                <i class="ph ph-trash"></i>
            </td>
    `;
    tab_book.appendChild(tr);
    list_search_close.click();
    count_change(tab_book.lastChild);
    price_change(tab_book.lastChild);
    count_keyup(tab_book.lastChild);
    price_keydown(tab_book.lastChild);
    del_item_ic_click(tab_book.lastChild);
    update_stt_sl_price();
}

const count_change = (tr) => {
    let num = tr.querySelector('.slnhap input');
    num.addEventListener('change', (e) => {
        if(e.target.value < 1) e.target.value = 1;
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
    let num = tr.querySelector('.slnhap input');
    num.addEventListener('keyup', (e) => {

        update_stt_sl_price();
        thanhtien_change(tr);
    })
}

const price_keydown = (tr) => {
    let price = tr.querySelector('.dongia input');
    price.addEventListener('keyup', (e) => {

        update_stt_sl_price();
        thanhtien_change(tr);
    })
}

const thanhtien_change = (tr) => {
    let num = tr.querySelector('.slnhap input').value;
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
        let soluong_s = tr[i].querySelector('.slnhap input').value;
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
            li.innerHTML = flag ? flow_ncc(item) : flow_gt(item);
            list_tag.appendChild(li);
        })
        list_tag.querySelectorAll('.btn-choose')
            .forEach(item => {
                item.addEventListener('click', (e) => {
                        if(flag) mem_child_ncc_click(e);
                        else mem_child_gt_click(e);
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


const flow_ncc = (data) => {
    const cus = `
      <figure>
        <p class="pic-ph">
            <button title="Chọn" data-set="${data.id}" class="btn-choose">Chọn</button>
        </p>
        <p title="Tên nhà cung cấp" class="name" 
            data-address="${data.diachi + ', ' + data.phuongxa + ', ' + data.quanhuyen + ', ' + data.tinhthanh}">
            ${data.ten}</p>
        <p title="Số điện thoại" class="phone">${data.sdt}</p>
        <p title="Email" class="email">${data.email}</p>
      </figure>
    `;
    return cus;
}

const flow_gt = (data) => {
    const cus = `
      <figure max-size="${data.soluong}">
        <p title="Ảnh" class="pic-ph">
            <img src="/pictures/book/${data.hinhanh}">
        </p>
        <p class="pic-ph">
            <button title="Chọn" data-set="${data.id}" class="btn-choose">Chọn</button>
        </p>
        <p title="Tên giáo trình" class="name">${data.ten}</p>
        <p title="Giá" class="phone" data-price="${data.gia}">${data.gia.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</p>
        <p title="Tồn kho" class="phone">${data.soluong}</p>
        <p title="Thuộc chuyên ngành" class="email">${data.chuyennganh.tenchuyennganh}</p>
      </figure>
    `;
    return cus;
}

inp_ncc.addEventListener('click', async (e) => {
    await inp_ncc_ev_click(e);
    inp_ev(e);
})
inp_gt.addEventListener('click', async (e) => {
    await inp_gt_ev_click(e);
    inp_ev(e);
})

const inp_ncc_keyup_ev = async (e) => {
    const list_ncc = e.target.parentNode.querySelector('.list-search ul');
    list_ncc.innerHTML = ``;
    await call_and_flow_ncc(e);
}

const inp_gt_keyup_ev = async (e) => {
    const list_gt = e.target.parentNode.querySelector('.list-search ul');
    list_gt.innerHTML = ``;
    await call_and_flow_gt(e)
}
inp_ncc.addEventListener('keyup', async (e) => await inp_ncc_keyup_ev(e));
inp_gt.addEventListener('keyup',  async (e) => await inp_gt_keyup_ev(e));


const input_all = body.querySelectorAll('.nhacungcap-inf input');
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
        alert('Chọn sách muốn nhập !');
        return;
    }

    if(!confirm('Xác nhận lập phiếu ?')) return;

    let nhacungcap_id = body.querySelector('input[name=nhacungcap_id]').value;
    let ghichu = body.querySelector('textarea[class=note]').value;
    let tr = tab_book.querySelectorAll('tr');
    let request_body = {
        nhacungcap_id: nhacungcap_id,
        ghichu: ghichu,
        chitietphieunhaps: []
    }

    tr.forEach(item => {
        request_body.chitietphieunhaps.push({
            soluong: item.querySelector('td input[name=soluong]').value,
            dongia: item.querySelector('td input[name=dongia]').value,
            giaotrinh_id: item.querySelector('td input[name=giaotrinh_id]').value
        });
    })

    console.log(request_body);
    loading();
    let result = null;
    let request = '/load/api/phieunhap/create';
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
                        <a href="/phieunhap">Hoàn tất</a>
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
const loading = () => {
    let mess_site = document.createElement('div');
    mess_site.className = 'mess-site';
    mess_site.innerHTML = `<img class="loadding" src="/pictures/main/pngtree-loading-icon-vector-transparent-png-image_5687537.png">`;
    body.appendChild(mess_site);
}

