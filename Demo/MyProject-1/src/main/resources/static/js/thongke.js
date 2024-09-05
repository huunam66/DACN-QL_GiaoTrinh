

let statsData = {
    // // Dữ liệu thống kê (ví dụ)
    // allLabels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
    thang: [],
    // orderCount: [50, 75, 60, 90, 80, 70, 100, 120, 110, 85, 95, 105],
    tongPhieuNhap: [],
    tongPhieuXuat: [],
    // revenueIncome: [3000, 4500, 3600, 5400, 4800, 4200, 6000, 7200, 6600, 5100, 5700, 6300],
    tongthu: [],
    // revenueOutcome: [2000, 3000, 2400, 3600, 3200, 2800, 4000, 4800, 4400, 3400, 3800, 4200]
    tongchi: []
};

let phieuChart, doanhThuChart;

function updateCharts(thongke, duatheo) {
    if (phieuChart && doanhThuChart) {
        if(thongke > 0 && duatheo > 0){
            phieuChart.data.labels = statsData.thang;
            doanhThuChart.data.labels = statsData.thang;
            if(thongke === 1){
                phieuChart.data.datasets[0].data = statsData.tongPhieuNhap;
                doanhThuChart.data.datasets[0].data = statsData.tongchi;
            }
            if(thongke === 2){
                phieuChart.data.datasets[0].data = statsData.tongPhieuXuat;
                doanhThuChart.data.datasets[0].data = statsData.tongthu;
            }
            if(thongke === 3){
                phieuChart.data.datasets[0].data = statsData.tongPhieuNhap;
                phieuChart.data.datasets[1].data = statsData.tongPhieuXuat;
                doanhThuChart.data.datasets[0].data = statsData.tongchi;
                doanhThuChart.data.datasets[1].data = statsData.tongthu;
            }
        }
        phieuChart.update();
        doanhThuChart.update();
    }
}

const reset_chart = () => {
    statsData.thang = [];
    statsData.tongPhieuNhap = [];
    statsData.tongPhieuXuat = [];
    statsData.tongthu = [];
    statsData.tongchi = []
    phieuChart.data.labels = [];
    phieuChart.data.datasets = [];
    doanhThuChart.data.labels = [];
    doanhThuChart.data.datasets = [];
}


let datasets_nx = [
    [
        [
            {
                label: 'Phiếu nhập',
                data: [],
                fill: false,
                backgroundColor: '#f39c12',
                borderWidth: 1
            }
        ],
        [
            {
                label: 'Phiếu xuất',
                data: [],
                backgroundColor: '#2ecc71',
                borderWidth: 1
            }
        ]
    ],
    [
        [
            {
                label: 'Tổng chi trả cho Phiếu nhập',
                data: [],
                backgroundColor: '#ff6384',
                borderWidth: 1
            }
        ],
        [
            {
                label: 'Tổng doanh thu từ Phiếu xuất',
                data: [],
                backgroundColor: 'rgb(75, 192, 192)',
                borderWidth: 1
            }
        ]
    ]
]

let datasets_cn = [
    [
        {
            label: 'Phiếu nhập',
            data: [],
            backgroundColor: '#f39c12',
            borderWidth: 1
        },
        {
            label: 'Phiếu xuất',
            data: [],
            backgroundColor: '#2ecc71',
            borderWidth: 1
        }
    ],
    [
        {
            label: 'Tổng chi trả cho Phiếu nhập',
            data: [],
            backgroundColor: '#ff6384',
            borderWidth: 1
        },
        {
            label: 'Tổng doanh thu từ Phiếu xuất',
            data: [],
            backgroundColor: 'rgb(75, 192, 192)',
            borderWidth: 1
        }
    ]
]

let max = 0;

// Khởi tạo biểu đồ số phiếu
phieuChart = new Chart(document.getElementById('phieu-chart').getContext('2d'), {
    type: 'bar',
    data: {
        labels: [],
        datasets: []
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        title: {
            display: true,
            text: 'Biểu đồ số phiếu nhập'
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    stepSize: () => {
                        let max_f_d_10 = max / 10;
                        let max_i_d_10 = Math.floor(max / 10);
                        if(max_f_d_10 <= 1)
                            return 1;

                        if(max_f_d_10 === max_i_d_10)
                            return max_i_d_10;

                        while(true){
                            max++;
                            if(max % 10 === 0)
                                break;
                        }
                        return Math.floor(max / 10);
                    }
                }
            }
        }
    }
});

// Khởi tạo biểu đồ doanh thu
doanhThuChart = new Chart(document.getElementById('doanhthu-chart').getContext('2d'), {
    type: 'bar',
    data: {
        labels: [],
        datasets: []
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        title: {
            display: true,
            text: 'Biểu đồ Doanh thu'
        },
        scales: {
            y:{
                ticks: {
                    beginAtZero: true
                }
            }
        }
    }
});


const host = 'http://localhost:8080';

const month_type = document.querySelector('.month-type');
const year_type = document.querySelector('.year-type');

const inject_time_current = () =>{
    let year_now = new Date().getFullYear();
    let month_now = new Date().getMonth() + 1;
    month_type.querySelector('.year').innerText = year_now;
    month_type.querySelector('.month').innerText = month_now;
    year_type.querySelector('.year').innerText = year_now;
}
inject_time_current();

const call_api_doanhthu = async (thongke, duatheo, year, month) => {
    let request = null;
    if(thongke === 1){
        request = `/load/api/thongke/phieunhap/nam?year=${year}`;
        if(duatheo === 1){
            request = `/load/api/thongke/phieunhap/thang?year=${year}&month=${month}`;
        }
    }
    if(thongke === 2){
        request = `/load/api/thongke/phieuxuat/nam?year=${year}`;
        if(duatheo === 1){
            request = `/load/api/thongke/phieuxuat/thang?year=${year}&month=${month}`;
        }
    }
    if(thongke == 3){
        request = `/load/api/thongke/nhapxuat/nam?year=${year}`;
        if(duatheo === 1){
            request = `/load/api/thongke/nhapxuat/thang?year=${year}&month=${month}`;
        }
    }
    // console.log(request);
    let result = null;
    await axios.get(host + request)
        .then(res => result = res)
        .catch(ex => console.log(ex));

    return result;
}

const chart_2_bot = document.querySelector('.chart-2-bot');
const inject_to_chart = async (thongke, duatheo, year, month) => {
    let result = await call_api_doanhthu(thongke, duatheo, year, month);
    if(result !== null && result.data !== null){
        reset_chart();
        max = 0;
        if(thongke === 1){
            chart_2_bot.querySelector('.title').innerText = 'Chi trả';
            phieuChart.options.title.text = 'Biểu đồ số Phiếu nhập';
            doanhThuChart.options.title.text = 'Biểu đồ tổng chi từ Phiếu nhập';
            result.data.forEach(item => {
                max += item.tongphieu;
                statsData.thang.push(item.thang);
                statsData.tongPhieuNhap.push(item.tongphieu);
                statsData.tongchi.push(item.tongchi.toString().split(',')[0]);
            })
            phieuChart.data.datasets = datasets_nx[0][0];
            doanhThuChart.data.datasets = datasets_nx[1][0];
        }
        if(thongke === 2){
            chart_2_bot.querySelector('.title').innerText = 'Doanh thu';
            phieuChart.options.title.text = 'Biểu đồ số Phiếu xuất';
            doanhThuChart.options.title.text = 'Biểu đồ tổng thu từ Phiếu xuất';
            result.data.forEach(item => {
                max += item.tongphieu;
                statsData.thang.push(item.thang);
                statsData.tongPhieuXuat.push(item.tongphieu);
                statsData.tongthu.push(item.tongthu.toString().split(',')[0]);
            })
            phieuChart.data.datasets = datasets_nx[0][1];
            doanhThuChart.data.datasets = datasets_nx[1][1];
        }
        if(thongke === 3){
            let max_nhap = 0;
            let max_xuat = 0;
            chart_2_bot.querySelector('.title').innerText = 'Thu chi';
            phieuChart.options.title.text = 'Biểu đồ số Phiếu';
            doanhThuChart.options.title.text = 'Biểu đồ tổng Thu chi';
            result.data.phieunhap.forEach(item => {
                max_nhap += item.tongphieu;
                statsData.thang.push(item.thang);
                statsData.tongPhieuNhap.push(item.tongphieu);
                statsData.tongchi.push(item.tongchi.toString().split(',')[0]);
            })
            result.data.phieuxuat.forEach(item => {
                max_xuat += item.tongphieu;
                statsData.tongPhieuXuat.push(item.tongphieu);
                statsData.tongthu.push(item.tongthu.toString().split(',')[0]);
            })
            max = max_nhap > max_xuat ? max_nhap : max_xuat;
            phieuChart.data.datasets = datasets_cn[0];
            doanhThuChart.data.datasets = datasets_cn[1];
        }
        updateCharts(thongke, duatheo);
    }
    else{
        reset_chart();
        updateCharts(thongke, duatheo);
    }
    // console.log(max % 10);
    // console.log(1.0 === 1);
}


const reset_sl_opt = (el) => {
    el.innerHTML = ``;
    let option = document.createElement('option');
    option.innerText = 'Dựa theo';
    option.value = 0;
    el.appendChild(option);
    year_type.classList.add('d-none');
    month_type.classList.add('d-none');
    reset_chart();
    updateCharts(0, 0);
}

const sl_opt = document.querySelector('.sl-opt');
reset_sl_opt(sl_opt);
const sl_tk = document.querySelector('.sl-tk');

const opt_date = document.querySelectorAll('.opt-date');
let ev_date_table_target = null;

const generate_year = (date_table, year_text, month_text, ic_calendar) => {
    if(month_text != null) {
        month_text.classList.remove('cl-red');
    }
    year_text.classList.add('cl-red');
    let name_type = date_table.querySelector('.name-type')
    name_type.innerText = 'Năm';
    let tag = year_text.parentNode.parentNode.querySelector('.tag');
    tag.innerHTML = '';
    for(let i = 2023; i >= 1990; i--){
        let p = document.createElement('p');
        p.innerText = i;
        let year_text_int = parseInt(year_text.innerText);
        if(i === year_text_int){
            p.classList.add('bgcl-0088ff');
        }

        p.addEventListener('click', async (e) => {
            year_text.innerText = e.target.innerText;
            if(month_text !== null){
                generate_month(date_table, year_text, month_text, ic_calendar);
                await inject_to_chart(
                    parseInt(sl_tk.value),
                    parseInt(sl_opt.value),
                    parseInt(year_text.innerText),
                    parseInt(month_text.innerText)
                );
                return;
            }
            await inject_to_chart(
                parseInt(sl_tk.value),
                parseInt(sl_opt.value),
                parseInt(year_text.innerText),
                null
            );
            date_table.classList.add('d-none');
            ic_calendar.classList.remove('cl-red');
        })
        tag.appendChild(p);
    }

    date_table.classList.remove('d-none');
    ic_calendar.classList.add('cl-red');
    date_table.classList.remove('month-style');
    date_table.classList.add('year-style');
}

const generate_month = (date_table, year_text, month_text, ic_calendar) => {
    year_text.classList.remove('cl-red');
    month_text.classList.add('cl-red');
    let name_type = date_table.querySelector('.name-type')
    name_type.innerText = 'Tháng';
    let tag = date_table.querySelector('.tag');
    tag.innerHTML = '';
    let month_text_int = parseInt(month_text.innerText);
    for(let i = 1; i <= 12; i++){
        let p = document.createElement('p');
        p.innerText = 'Tháng ' + i;
        if(i === month_text_int){
            p.classList.add('bgcl-0088ff');
        }
        p.addEventListener('click', async (e) => {
            let value_month_text_split_number = e.target.innerText.split(' ')[1].trim();
            let value_month_int = value_month_text_split_number.split('').length < 2 ? '0' + value_month_text_split_number.toString() : value_month_text_split_number.toString();
            month_text.innerText = value_month_int;
            await inject_to_chart(
                parseInt(sl_tk.value),
                parseInt(sl_opt.value),
                parseInt(year_text.innerText),
                parseInt(month_text.innerText)
            );
            date_table.classList.add('d-none');
            ic_calendar.classList.remove('cl-red');
        })
        tag.appendChild(p);
    }

    date_table.classList.remove('d-none');
    ic_calendar.classList.add('cl-red');
    date_table.classList.remove('year-style');
    date_table.classList.add('month-style');
}

opt_date.forEach(item => {
    let date_table = item.querySelector('.date-table');
    let year_text = item.querySelector('.year');
    let month_text = item.querySelector('.month');
    let ic_calendar = item.querySelector('.bxs-calendar');

    year_text.addEventListener('click', (e) => {
        generate_year(date_table, e.target, month_text, ic_calendar);
    })
    if(month_text !== null){
        month_text.addEventListener('click', (e) => {
            generate_month(date_table, year_text, e.target, ic_calendar);
        })
    }
    ic_calendar.addEventListener('click', (e) => {
        let year_text_red = year_text.classList.contains('cl-red');
        let month_text_red = month_text !== null ? month_text.classList.contains('cl-red') : false;
        if(year_text_red && month_text_red){
            year_text.click();
            return;
        }
        if(year_text_red) {
            year_text.click();
            return;
        }
        if(month_text_red) {
            month_text.click();
            return;
        }
        year_text.click();
    })

    item.querySelectorAll('*').forEach(i => {
        i.addEventListener('click', (e) => {
            e.stopPropagation();
            let date_table_in = item.querySelector('.date-table');
            if(ev_date_table_target !== null && ev_date_table_target !== date_table_in){
                ev_date_table_target.classList.add('d-none');
                ev_date_table_target.parentNode.querySelector('.bxs-calendar').classList.remove('cl-red');
            }

            ev_date_table_target = item.querySelector('.date-table');
        })
    })
})

document.addEventListener('click', (e) => {
    // console.log(ev_date_table_target);
    if(ev_date_table_target !== null){
        // console.log(e);
        // console.log(ev_date_table_target);
        if(e.target !== ev_date_table_target) {
            ev_date_table_target.classList.add('d-none');
            ev_date_table_target.parentNode.querySelector('.bxs-calendar').classList.remove('cl-red');
        }
    }
})

sl_tk.addEventListener('change', (e) => {
    if(parseInt(e.target.value) === 0){
        reset_sl_opt(sl_opt);
        reset_chart();
        updateCharts(0, 0);
    }
    else{
        sl_opt.innerHTML = `
            <option value="0" selected>Dựa theo</option>
            <option value="1">Theo tháng</option>
            <option value="2">Cả năm</option>
        `;
        reset_chart();
        updateCharts(parseInt(e.target.value), parseInt(sl_opt.value));
        year_type.classList.add('d-none');
        month_type.classList.add('d-none');

    }
})

sl_opt.addEventListener('change', async (e) => {
    let duatheo_value = parseInt(e.target.value);
    if(duatheo_value === 0){
        reset_chart();
        updateCharts(parseInt(sl_tk.value), 0);
        month_type.classList.add('d-none');
        year_type.classList.add('d-none');
        return;
    }

    let thongke_value = parseInt(sl_tk.value);

    if(duatheo_value === 1){
        month_type.classList.remove('d-none');
        year_type.classList.add('d-none');
        let year = parseInt(month_type.querySelector('.year').innerText);
        let month = parseInt(month_type.querySelector('.month').innerText);
        await inject_to_chart(thongke_value, duatheo_value, year, month);
        return;
    }

    if(duatheo_value === 2){
        year_type.classList.remove('d-none');
        month_type.classList.add('d-none');
        let year = parseInt(year_type.querySelector('.year').innerText);
        await inject_to_chart(thongke_value, duatheo_value, year, null);
    }
})
