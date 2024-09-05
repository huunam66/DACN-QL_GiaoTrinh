// const body = document.querySelector('body');

const constraint = () => {
    const inp_item = $('.box-imp input:not([type=hidden])');
    const select_item = $('.box-imp select');
    const flag_on = true;
    const flag_off = false;

    $('button[type=button]').on('click', function(e){
        // console.log(e.target.innerHTML);
        let flag = flag_off;

        inp_item.each((index, item) => {
            if($(item).val() == ''){
                let text_err = `${$(item).siblings('label').html().split(':')[0].trim()} là bắt buộc !`;
                $(item).siblings('.error').html(text_err);
                flag = flag_on;
            }
            else{
                $(item).siblings('.error').html('');
            }
        })

        select_item.each((index, item) => {
            let value_item = $(item).find(':selected').val();
            if(value_item.toLowerCase() === '#'){
                $(item).siblings('.error').html(
                    `
                        Chọn ${$(item).siblings('label').html().split(':')[0].trim().toLowerCase()} !
                    `
                );
                flag = flag_on;
            }
            else{
                $(item).siblings('.error').html('');
            }
        })

        if(!flag){
            $(this).attr('type', 'submit');
        }
        else{
            $(this).attr('type', 'button');
        }
    })
}
constraint();

const picture = body.querySelector('.box-img-rr picture');
const img_load = picture.querySelector('.img-load');
const btn_img_cl = body.querySelector('.btn-img-cl-b');
const img_inp = body.querySelector('.box-img-rr input[type=file]');

img_inp.addEventListener('change', (e) => {
    let fReader = new FileReader();
    fReader.readAsDataURL(e.target.files[0]);
    fReader.onloadend = (ev) => {
        img_load.setAttribute('src', ev.target.result);
    }
})

picture.addEventListener('click', () => {img_inp.click()});
btn_img_cl.addEventListener('click', () => {img_inp.click()});



const mess_ss = () => {
    const ss = $('.box-sm p').html();
    if(ss.toLowerCase().includes('thành công')){
        $('body').addClass('ov-hd');
        const mess_box = $('.mess-box');
        if(mess_box.length === 0){
            // console.log('asf');
            $('<div>', {
                class: 'mess-site',
                html: `
                    <div class="mess-box">
                        <p class="mess-text">${ss}</p>
                        <img type="hidden" style="display: none;" src="/pictures/main/icons8-success-48.png">
                        <img class="ic-ss" src="/pictures/main/icons8-success.gif">
                        <p class="btn-cls">
                            <a href="/book">Hoàn tất</a>
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
                .on('click', function() {
                    $(this).closest('.mess-site').remove();
                    $('body').removeClass('ov-hd');
                })
        }
    }
}
mess_ss();