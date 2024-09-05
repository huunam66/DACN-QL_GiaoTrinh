
const inp_ncc = body.querySelector('.inp-ncc');
const inp_gt = body.querySelector('.inp-gt');
let inp_search_e = null;
let list_search_v = null;
let list_search_close = null;
const inp_ev = (e) =>{
    let list_search = e.target.parentNode.querySelector('.list-search');
    if(list_search.classList.contains('list-search-unhide')) return;
    // console.log(e);
    if(list_search_close != null) list_search_close.click();
    inp_search_e = e;
    list_search_v = list_search;
    list_search.classList.add('list-search-unhide');
    list_search.addEventListener('click', () => {
        inp_search_e.target.click();
    })
    list_search_close = list_search.querySelector('.close');
    list_search_close.addEventListener('click', (eve) => {
        eve.stopPropagation();
        let list_search_unhide = list_search.classList.contains('list-search-unhide');
        if(list_search_unhide)
            list_search.classList.remove('list-search-unhide');
    })
}

const click_out = (e) => {
    if (inp_search_e != null
        && !e.target.isSameNode(inp_search_e.target)) {
        if (list_search_v != null
            && !e.target.isSameNode(list_search_v)) {
            if(!list_search_v.contains(e.target)){
                list_search_close.click();
            }
        }
    }
}
document.addEventListener('click', (e) => {
    click_out(e);
})

const left_t_back = document.querySelector('.top-mod .left-t');
left_t_back.addEventListener('click', (e) => {
    if(confirm(e.target.innerText.toLowerCase() + ' ?'))
        return;

    e.preventDefault();
})