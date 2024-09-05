// const body = document.querySelector('body');

// get api to inject combobox province & district & ward
const inject_option = async (host, request, element, type) => {

    let result = null;
    // nhận được một Promise ngay khi request
    await axios.get(host + request)
        .then(res => result = res)
        .catch(ex => result = ex);

    reset_cbb(element);
    if(result.data != null){
        let data = result.data;
        if(type === 'district') data = result.data.districts;
        if(type === 'ward') data = result.data.wards;
        data.forEach(item => {
            let option = document.createElement('option');
            option.value = item.name;
            option.id = `id-${item.code}`;
            option.innerHTML = item.name;
            element.appendChild(option);
        })
    }
    else{
        console.log(result);
    }
}

const reset_cbb = (element) => {
    element.innerHTML = ``;
    let option = document.createElement('option');
    option.value = '#';
    option.innerHTML = 'Chọn';
    element.appendChild(option);
}

const host = 'https://provinces.open-api.vn';
const cbb_provinces = body.querySelector('.provinces');
const cbb_districts = body.querySelector('.districts');
const cbb_wards = body.querySelector('.wards');
inject_option(host, '/api/?depth=1', cbb_provinces);

cbb_provinces.addEventListener('change', (e) => {
    let value = e.target.value;
    if(value !== '#'){
        let target = e.target;
        let index = target.selectedIndex;
        let options = target.options;
        let id = options[index].id.split('-')[1].trim();
        inject_option(host, `/api/p/${id}?depth=2`, cbb_districts, 'district');
        reset_cbb(cbb_wards);
    }
    else{
        reset_cbb(cbb_districts);
        reset_cbb(cbb_wards);
    }
})

cbb_districts.addEventListener('change', (e) => {
    let value = e.target.value;
    if(value !== '#'){
        let target = e.target;
        let index = target.selectedIndex;
        let options = target.options;
        let id = options[index].id.split('-')[1].trim();
        inject_option(host, `/api/d/${id}?depth=2`, cbb_wards, 'ward');
    }
    else{
        reset_cbb(cbb_wards);
    }
})