function toResultPage(e) {
    window.sessionStorage.removeItem("keywords");
    window.sessionStorage.setItem("keywords",e.lastChild.innerHTML);
    location.href="result.html?keywords="+e.lastChild.innerHTML;
}

/* 搜索 */
var exceptionSearch={
    /* 元素集 */
    els:{},
    /* 搜索类型序号 */
    searchIndex:0,
    /* 火热的搜索列表 */
    hot:{
        /* 颜色 */
        color:['#ff2c00','#ff5a00','#ff8105','#fd9a15','#dfad1c','#6bc211','#3cc71e','#3cbe85','#51b2ef','#53b0ff'],
        /* 列表 */
        list:[
            'java',
            'thymeleaf',
            'python',
            'c'
        ]
    },
    /* 初始化 */
    init:function(){
        var _this=this;
        this.els={
            pickerBtn:$(".picker"),
            pickerList:$(".picker-list"),
            logo:$(".logo"),
            hotList:$(".hot-list"),
            input:$("#search-input"),
            button:$(".search")
        };
        setSearchList();

        /* 设置热门搜索列表 */
       function setSearchList() {
           $(".hot-list").html(function () {
               var str='';
               $.each(_this.hot.list,function (index,item) {
                   window.sessionStorage.setItem("keyword",item)
                   str+='<a onclick="toResultPage(this)"  target="_blank">'
                       +'<div class="number" style="color: '+_this.hot.color[index]+'">'+(index+1)+'</div>'
                       +'<div id="ItemContent">'+item+'</div>'
                       +'</a>';
               });
               return str;
           });
       }

        /* 注册事件 */
        /* 搜索类别选择按钮 */
        this.els.pickerBtn.click(function () {
            if(_this.els.pickerList.is(':hidden')) {
                setTimeout(function () {
                    _this.els.pickerList.show();
                },100);
            }
        });

        /* 搜索 输入框 点击*/
        this.els.input.click(function () {
            if(!$(this).val()){
                // setTimeout(function () {
                //     _this.els.hotList.show();
                // },100);
            }
        });
        /* 搜索 输入框 输入*/
        this.els.input.on("input",function () {
            if($(this).val()){

                $.ajax({
                    url:"searchAssociation",
                    type:"GET",
                    data:{"keywords":$(this).val()},
                    success:function (res) {
                        _this.hot.list = res;
                    }
                })
                setSearchList();
                _this.els.hotList.show();
            }
        });
        /* 搜索按钮 */
        this.els.button.click(function () {
            var keywords = $("#search-input").val();
            location.href = "result.html?keywords="+keywords;
            window.sessionStorage.setItem("keywords",keywords);
        });
        /* 文档 */
        $(document).click(function () {
            _this.els.pickerList.hide();
            _this.els.hotList.hide();
        });
        /* 搜索按钮 */
    }
};
