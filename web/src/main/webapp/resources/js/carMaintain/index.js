var _carId;
var _searchMile;
var _msg = '';
var _body = $('body');
var _maxMile = null;


$(document).ready(function(){
    App.init(); // 初始化插件

    getCarBrand();

    $('#searchBtn').click(function(){
        searchMaintain();
    });

    $('#searchMile').keyup(function(event){
        //alert(event.keyCode);
        if (event.keyCode == 13) {
            $(this).val(this.value.replace(/\D/g, ''));
            $('#searchBtn').trigger('click');
            return false;
        }

        if(/^\d+$/g.test(this.value)){
            return false;
        }

        $(this).val(this.value.replace(/\D/g, ''));
    });

});

function searchMaintain(){
    var searchMile = $('#searchMile').val().replace(/\D/g, '');
    if(searchMile==''){
        searchMile = 0;
    }
    $('#searchMile').val(Number(searchMile));

    var carIdStr = $('#carSelect').val();
    if(carIdStr=='-1'){
        alert('请先选择车款');
        return;
    }

    var idx = carIdStr.indexOf(',');
    var carId = carIdStr.substring(0, idx);

    if(_carId==carId){
        if(_searchMile==searchMile){
            if(_msg!=''){
                alert(_msg);
            }

            handleScroll();
            return;
        }else{
            if(_maxMile!=null && searchMile>_maxMile){
                alert("当前车款保养里程表只提供0至" + _maxMile + "公里的保养数据，请您见谅!");

                handleScroll();
                return;
            }
        }
    }

    getMaintainDetail(carId, searchMile, carIdStr.substring(idx+1));
}

/* 查询保养详情 */
function getMaintainDetail(carId, mileage, carModel){
    App.blockUI(_body);

    var selectedCar = carModel+' '+$('#carSelect').select2('data').text+' 保养项目';
    $.ajax({
        url: '/monkey/maintain/detail',
        data: {carId: carId, mileage: mileage},
        success: function(result){
            _carId = carId;
            _searchMile = mileage;

            if(result.success){
                _msg = '';
                result.data['selectedCar'] = selectedCar;
                var html = template('maintainDetailTemplate', result.data);
                $('#maintainDetail').html(html);
                $('#commonMaintain').html(template('commonMaintainTemplate', result.data));
                handleScroll();
                handleMaxMile(result.data);
            }else{
                _msg = result.message;
                _maxMile = result.data;
                alert(_msg);
            }

            App.unblockUI(_body);
        }
    });
}

function handleMaxMile(data){
    var dataArray = data.maintainDetail[0];
    _maxMile = dataArray[dataArray.length-1];
}

/* 处理滚动条 */
function handleScroll(){
    var content = $('#maintainDetailTable');
    var scrollLeft = content.scrollLeft();
    if(scrollLeft==null){
        return;
    }

    var contentWidth = content.width();
    var contentLeft = content.offset().left;

    var target = $('.mile-highlight').eq(0);
    var targetLeft = target.offset().left;

    var left = targetLeft - contentLeft + scrollLeft - contentWidth/2;
    //alert(left);
    content.animate({scrollLeft: left}, 500);
}

/* 车款筛选 */
function getCarBrand(){
    App.blockUI(_body);

    $.ajax({
        url: '/monkey/car/getCarByPid',
        data: {pid: 0},
        success: function (result) {
            var html = template('carBrandTemplate', result);
            $('#brandSelect').html(html).change(function(){
                getCarModels(this.value);
            });

            App.unblockUI(_body);
        }
    });
}

function getCarModels(brandId){
    $('#modelSelect').select2('val', '-1');
    $('#carSelect').select2('val', '-1');
    $('#carSelect').get(0).options.length = 1;

    App.blockUI(_body);

    $.ajax({
        url: '/monkey/maintain/carModels',
        data: {brandId: brandId},
        success: function (result) {
            var html = template('carModelTemplate', result);
            $('#modelSelect').html(html).unbind().change(function(){
                getCars(this.value);
            });

            App.unblockUI(_body);
        }
    });
}

function getCars(modelId){
    $('#carSelect').select2('val', '-1');

    App.blockUI(_body);

    $.ajax({
        url: '/monkey/maintain/cars',
        data: {modelId: modelId},
        success: function (result) {
            var html = template('carTemplate', result);
            $('#carSelect').html(html);

            App.unblockUI(_body);
        }
    });
}
