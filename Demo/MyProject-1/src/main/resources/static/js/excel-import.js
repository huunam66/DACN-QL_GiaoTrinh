let success = false;
const blur_excel_func = () => {
    const blur_excel = document.createElement('div');
    blur_excel.classList.add('blur-excel');
    blur_excel.innerHTML = `
         <div class="content">
                <i class="ph ph-x"></i>
                <p class="top">
                    <input type="file" hidden="hidden">
                    <span class="value-file">File EXCEL</span>
                    <button class="c-file">Chọn tệp</button>
                </p>
                <p class="bot">
                    <!-- Đọc file thành công ! -->
                    <!-- <button class="sm">Upload</button> -->
                </p>
                <!-- <div class="loadding">
                    <img src="./pngtree-loading-icon-vector-transparent-png-image_5687537.png">
                </div> -->
            </div>
        `;
    return blur_excel;
}

const button_import_excel_click = () => {

    document.querySelector('body').appendChild(blur_excel_func());

    const ic_close_import_excel = document.querySelector('.blur-excel .ph-x');
    const ic_close_import_excel_click = (e) => {
        if(success) window.location.href = window.location.href;

        const blur_excel = e.target.parentNode.parentNode;
        document.querySelector('body').removeChild(blur_excel);
    }

    ic_close_import_excel.addEventListener('click', ic_close_import_excel_click);

    const input_file = document.querySelector('.blur-excel [type=file]');

    const value_file = document.querySelector('.blur-excel .value-file');
    value_file.addEventListener('click', e => input_file.click());
    const c_file = document.querySelector('.blur-excel .c-file');
    c_file.addEventListener('click', e => input_file.click());

    input_file.addEventListener('change', e => input_file_change(e))
}
const button_import_excel = document.querySelector('.out-file-excel');
button_import_excel.addEventListener('click', e => button_import_excel_click());

const input_file_change = (e) => {
    if(e.target.files.length < 1) return;
    let value_file = e.target.parentNode.querySelector('.value-file');
    value_file.innerText = e.target.files[0].name;
    let content_tag = e.target.parentNode.parentNode;
    let bot_tag = content_tag.querySelector('.bot');
    bot_tag.innerHTML = ``;
    let button_sm_tag = bot_tag.querySelector('.sm');
    let _this = e.target;
    if(button_sm_tag == null){
        button_sm_tag = document.createElement('button');
        button_sm_tag.classList.add('sm');
        button_sm_tag.innerText = 'Upload';
        bot_tag.appendChild(button_sm_tag);
        button_sm_tag.addEventListener('click', async (e) => {
            let loadding_bag = document.createElement('div');
            loadding_bag.classList.add('loadding');
            loadding_bag.innerHTML = `
                    <img src="/pictures/main/pngtree-loading-icon-vector-transparent-png-image_5687537.png">
                `;
            content_tag.appendChild(loadding_bag);
            await post_api_to_reading(_this.files[0]);

        })
    }
}

const post_api_to_reading = async (file) => {
    let host = 'http://localhost:8080';
    let request = '/load/api/lop/excel/read';
    let config = {
        "headers": {
            "content-type": "multipart/form-data"
        }
    }
    let formData = new FormData();
    formData.append('file', file);
    let result = null;
    await axios.post(host + request, formData, config)
        .then(res => result = res)
        .catch(ex => result = ex);

    if(result != null && result.data != null){
        if(result.data.status == 'isValid') success = true;

        let color = result.data.status == 'isValid' ? 'green' : 'red';
        let blur_excel_bot = document.querySelector('.blur-excel .bot');
        let button_sm = blur_excel_bot.querySelector('.sm');
        if(button_sm != null){
            blur_excel_bot.removeChild(button_sm);
        }
        let span = document.createElement('span');
        span.innerText = result.data.content;
        span.classList.add(color);
        blur_excel_bot.appendChild(span);
        let blur_excel_content = blur_excel_bot.parentNode;
        blur_excel_content.removeChild(
            blur_excel_content.querySelector('.loadding')
        );
    }
}
