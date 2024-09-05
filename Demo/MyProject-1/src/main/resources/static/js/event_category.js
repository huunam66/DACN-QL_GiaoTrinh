const body = document.querySelector('body');

//---------------------------------------
// event for show on, show off category
//---------------------------------------

let nav = document.querySelector('.nav');
let current_select = null;
const show_on = () => {
    let class_mate = nav.classList;

    //-------------------------------------------------

    if(class_mate.contains('show-off')){
        class_mate.remove('show-off');
        class_mate.add('show-on');
        if(current_select != null){
            current_select.classList.add('select-nav');
        }
        return;
    }
    //
    // if(class_mate.contains('origin')){
    //     if(current_select != null){
    //         current_select.classList.remove('select-nav');
    //     }
    //     class_mate.remove('origin');
    //     class_mate.add('show-off');
    //     return;
    // }


    class_mate.remove('show-on');
    class_mate.add('show-off');
    if(current_select != null){
        current_select.classList.remove('select-nav');
    }
}

const btn_show = body.querySelector('.btn-shw');
btn_show.addEventListener('click', show_on);

//--------------------------------------------
// event for drop down ele nav
//--------------------------------------------

$(document).ready(() => {    
    $('.drop-clk').each((index, item) => {
        if($(item).parent().hasClass("select-nav")){
            current_select = $(item)[0].parentNode;
        }

        $(item).on('click', () => {

            let nav_show_off = nav.classList.contains('show-off');
            if(nav_show_off){
                btn_show.click();
            }

            let parent = $(item)[0].parentNode;
            let dropped = $(item).parent().hasClass('select-nav');
         
            if(dropped && !nav_show_off){
                $(item).parent().removeClass('select-nav');
                $(item).parent().removeClass('select-nav-bgcl');
                current_select = null;
            }
            else{
                if(current_select != null){
                    current_select.classList.remove('select-nav');
                    current_select.classList.remove('select-nav-bgcl');
                }

                $(item).parent().addClass('select-nav');
                $(item).parent().addClass('select-nav-bgcl');
                current_select = parent;
            }
        });
    });
})    


//const iframe = body.querySelector('iframe');
//const link_page = body.querySelectorAll('.drop-list-item .link');
//const evt_link_page = (item) => {
//    item.addEventListener('click', () => {
//        iframe.src = item.getAttribute('data-set');
//    })
//}
//
//link_page.forEach(item => evt_link_page(item));