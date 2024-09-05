// const body = document.querySelector('body');

const constraint = () => {
    const inp_item = $('input:not([type=hidden])');
    const select_item = $('select');
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
