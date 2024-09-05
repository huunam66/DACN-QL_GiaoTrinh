
const host = 'http://localhost:8080';
const config = {
    "headers": {
        "content-type": "application/json"
    }
}


const btn_duyet = document.querySelector('.layout-main-site .duyet');
if(btn_duyet != null){
    btn_duyet.addEventListener('click', async (e) => {
        if(!confirm('Xác nhận duyệt phiếu ?')) return;

        loading();

        let id = e.target.parentNode.querySelector('input[name="id-phieu"]').value;

        let request = '/load/api/admin/duyet/phieu/nhap';
        let request_body = JSON.stringify({
            id: id,
            tinhtrang: 'Đã duyệt'
        });
        let result = null;
        await axios.put(host + request, request_body, config)
            .then(res => result = res)
            .catch(ex => console.log(ex));

        if(result != null && result.data != null){
            mess_box(result.data);
        }
    })
}

const btn_huy = document.querySelector('.layout-main-site .huy');
if(btn_huy != null){
    btn_huy.addEventListener('click', async (e) => {
        if(!confirm('Xác nhận hủy phiếu ?')) return;

        loading();

        let id = e.target.parentNode.querySelector('input[name="id-phieu"]').value;

        let request = '/load/api/admin/huy/phieu/nhap';
        let request_body = JSON.stringify({
            id: id,
            tinhtrang: 'Đã hủy'
        });
        let result = null;
        await axios.put(host + request, request_body, config)
            .then(res => result = res)
            .catch(ex => console.log(ex));

        if(result != null && result.data != null){
            mess_box(result.data);
        }
    })
}

const loading = () => {
    let mess_site = document.createElement('div');
    mess_site.className = 'mess-site';
    mess_site.innerHTML = `<img class="loadding" src="/pictures/main/pngtree-loading-icon-vector-transparent-png-image_5687537.png">`;
    body.appendChild(mess_site);
}

const mess_box = (result) => {
    let loading_mess = body.querySelector('.mess-site');
    body.removeChild(loading_mess);
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

            window.location.href = window.location.href;
        }, 1500)
    }, 1200);
}
