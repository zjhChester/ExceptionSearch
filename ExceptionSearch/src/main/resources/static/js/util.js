
    //初始化结果页
    function init(keywords,type) {
        $(".load").show()
        $.ajax({
            url:"searchCount",
            type:"GET",
            data:{"keywords":keywords,"type":type},
            success:function (count) {
                var searchCount = count;
                //初始化页面 第一次加载第一页
                search(type,keywords,1);
                //加载搜索结果
                $("#num").html(searchCount.code)
                //显示搜索时间
                $("#time").html(searchCount.desc);
                var pageTotoalCount = parseInt(Number(searchCount.code)%20==0?Number(searchCount.code)/20:Number(searchCount.code)/20+1);
                new myPagination({
                    id: 'pagination',
                    curPage:1, //初始页码
                    pageTotal: pageTotoalCount==0?1:pageTotoalCount, //总页数
                    pageAmount: 20,  //每页多少条
                    dataTotal: searchCount.code, //总共多少条数据
                    pageSize: 5, //可选,分页个数
                    showPageTotalFlag:true, //是否显示数据统计
                    showSkipInputFlag:true, //是否支持跳转
                    getPage: function (page) {
                        //获取当前页数
                        $(".load").show()
                        search(type,keywords,page);
                    }
                })
            }
        })
    }

    //搜索
    function search(type,keywords,page){
        $(".load-msg").html("正在加载，请稍后")
        $.ajax({
            url:"search",
            type:"GET",
            data:{"keywords":keywords,"type":type,"currPage":page},
            success:function(res){
                //清空页面
                $(".search-result").html("");
                var e = res;

                //文字高亮   解决方案2  先把结果内容转小写 去匹配关键字的小写，匹配到了记录index,str.length 在原结果串取出来，再进行replace()
               //1、取出关键词的小写
                if(e.result != undefined){
                    //转小写
                    var lowercaseKeywords = e.desc.toLowerCase().split(",");
                    for (var i = 0; i < e.result.length; i++) {
                        //2、取结果串的小写
                        var lowerResContent = e.result[i].title.toLowerCase();
                        //3、匹配
                        //找到后装到index[]
                        var index=[];
                        for (var j = 0; j < lowercaseKeywords.length; j++) {
                            index.push(lowerResContent.indexOf(lowercaseKeywords[j]));
                        }
                        // 如果index!=-1 取出原串的值 然后替换 装入需要高亮的原关键词组
                        var keywordsFromRes = []
                        for (var j = 0; j < index.length; j++) {
                            if(index[j] != -1){
                                //截取的截止部位是拿到的关键词串数组中的串的长度
                                keywordsFromRes.push(e.result[i].title.substr(index[j],Number(lowercaseKeywords[j].length)));
                            }
                        }
                        //进行替换
                        var title= e.result[i].title;
                        var type = e.result[i].type;
                        var desc = e.result[i].desc;
                        for (var j = 0; j < keywordsFromRes.length; j++) {
                            title = title.replace(new RegExp(keywordsFromRes[j],'g') ,"<em>"+keywordsFromRes[j]+"</em>");
                            type = type.replace(new RegExp(keywordsFromRes[j],'g') ,"<em>"+keywordsFromRes[j]+"</em>");
                            desc = desc.replace(new RegExp(keywordsFromRes[j],'g') ,"<em>"+keywordsFromRes[j]+"</em>");
                        }
                        var author = e.result[i].author;
                        var views = e.result[i].views;
                        $(".search-result").append("<div class=\"resultException\" style=\"\">\n" +
                            "\t\t\t\t\t<div class=\"col-xs-2\">\n" +
                            "\t\t\t\t\t\t<p></p>\n" +
                            "\t\t\t\t\t</div>\n" +
                            "\t\t\t\t\t<div class=\"col-xs-10 text\" >\n" +
                            "\t\t\t\t\t\t<div class=\"col-xs-8 \">\n" +
                            "\t\t\t\t\t\t\t<a target='_blank'style='cursor: pointer' onclick='toDetailPage("+e.result[i].id+")' class=\"title\">"+title+"</a>\n" +
                            "\t\t\t\t\t\t</div>\n" +
                            "\t\t\t\t\t\t<div class=\"col-xs-4\">\n" +
                            "\t\t\t\t\t\t\t<p class=\"type\">类型："+type+"</p><p class=\"views\">访问量："+views+"</p>\n" +
                            "\t\t\t\t\t\t</div>\n" +
                            "\t\t\t\t\t\t<hr style=\"border: none;\">\n" +
                            "\t\t\t\t\t\t<div class=\"col-xs-12 \" style=\"font-size: 1.1em; width:80%;margin-bottom: 10%;border-bottom: #adadad  solid 0.5px\">\n" +
                            "\t\t\t\t\t\t\t<p class=\"desc\">"+desc+"</p>\n" +
                            "\t\t\t\t\t\t\t<p  class=\"text-right author\">作者："+author+"" +
                            "\t\t\t\t\t\t</div>\n" +
                            "\t\t\t\t\t</div>\n" +
                            "\t\t\t\t</div>")
                    }
                }
                $(".load").hide()
            },error:function(res){

            }
        })
    }

    //登录状态
    function userStatus() {
        $.ajax({
            url:"userStatus",
            type:"GET",
            success:function (res) {
                var e = res;
                if(e.code == 1){
                    $(".user").html("<a style='margin-right: 2%' href='user.html'>"+res.result[0].username+"</a>")
                    $(".user").append("<button  type=\"button\" class=\"btn btn-default exitBtn\" data-toggle=\"modal\"  onclick=\"exit()\" data-target=\".myModalLogout\">退出登录</button>\n")
                    $(".user").append("\t\t\t\t<a href=\"user.html\"><button class=\"btn btn-info\">个人中心</button></a>\n")
                }else{
                    $(".user").html("<button  type=\"button\" class=\"btn btn-default \" data-toggle=\"modal\"onclick=\"login()\" data-target=\".myModalLogin\">登录</button>\n");
                }
            }
        })
    }

    //模态登录框
    function login() {
        //执行函数
        $("#myModal_enterLogin").click(function () {
            if ($("input[name='username']").val() == "") {
                $("input[name='username']").focus();
                return;
            } else if ($("input[name='password']").val() == "") {
                $("input[name='password']").focus();
                return;
            } else {
                //发起登陆请求
                $.ajax({
                    url: "checkUser",
                    type: "POST",
                    data: {"username": $("input[name='username']").val(), "password": hex_md5($("input[name='password']").val())},
                    success: function (res) {
                        $("input[name='username']").val("");
                        $("input[name='password']").val("");
                        if (res.code == 1) {
                            $(".user").html("<a  style='margin-right: 2%' href='user.html'>"+res.result[0].username+"</a>")
                            $(".user").append("<button  type=\"button\" class=\"btn btn-default \"data-toggle=\"modal\" onclick=\"exit()\" data-target=\".myModalLogout\">退出登录</button>\n")
                            $(".user").append("\t\t\t\t<a href=\"user.html\"><button class=\"btn btn-info\">个人中心</button></a>\n")
                            $("#cancelLogin").click();
                        }else{
                            alert("登陆失败，原因："+res.desc)
                        }
                    }
                })
            }
        });
    }
    function exit() {
        //执行函数
        $("#myModal_enter").click(function() {
            $.ajax({
                url: "userExit",
                type: "POST",
                success: function (res) {
                    if (res.code == 1) {
                        $(".user").html("<button  type=\"button\" class=\"btn btn-default \" data-toggle=\"modal\"onclick=\"login()\" data-target=\".myModalLogin\">登录</button>\n");
                        $("#cancelExit").click();
                        if(createMarkdownUserStatus != undefined){
                            createMarkdownUserStatus()
                        }
                    }else{
                        alert("下线失败！")
                    }
                }
            });
        });

    }
    //详情页
    function toDetailPage(e) {
        // window.sessionStorage.removeItem("id");
        // window.sessionStorage.setItem("id",e);

        window.open("markdownDetail.html?id="+e,"_blank");
    }
    function loadException(id){
        $.ajax({
            url:"exception/"+id,
            type:"GET",
            success:function(res){
                var e = res;
                if(e.code == 1){
                    $("title").html(e.result[0].title)
                    $(".keywords").html(e.result[0].title)
                    $("#title").html(e.result[0].title);
                    $("#author").html("作者："+e.result[0].author);
                    $("#type").html("标签："+e.result[0].type);
                    $(".viewsTab").html("views:"+e.result[0].views)
                    convert(e.result[0].content);
                    //加载结束
                    $(".load").hide()
                }else{
                    alert("没有当前博文哦！原因是:"+e.desc)
                    $(".load").hide()
                    window.location.href="index.html";
                }
            }
        })
    }
    function convert(text) {
        var converter = new showdown.Converter({
            tables: true
        });
        var html = converter.makeHtml(text);
        $(".content").html(html);
    }
    function createMarkdownUserStatus() {
        $(".load").show()
        $.ajax({
            url:"userStatus",
            type:"GET",
            success:function (res) {
                var e = res;
                if(e.code == 0){
                    alert("还未登陆，请先登录哦！")
                    location.href="index.html"
                }else{
                    //判断登录状态输出结果到.user
                    userStatus()
                    //移除退出按钮

                }
                $(".load").hide()
            }
        });
    }
    /**
     * favorite
     */
    //查询是否收藏
    function favoriteStatus(id) {
        $.ajax({
            url: "isFavByUsernameAndExceptionId",
            type:"GET",
            data:{"id":id},
            success:function (res) {
                if(res == 1){
                    //已经收藏了  点击是取消收藏
                    $(".favoriteBtn").html("已收藏");
                    $(".favoriteBtn").attr("onclick","favoriteCancel("+location.href.toString().substr(location.href.indexOf("?")+1).split("=")[1]+")")
                }else{
                    //没收藏 点击是添加收藏
                    $(".favoriteBtn").html("收藏该文");
                    $(".favoriteBtn").attr("onclick","addFavorite("+location.href.toString().substr(location.href.indexOf("?")+1).split("=")[1]+")")
                }
            }
        })
    }
    //收藏
    function addFavorite(id) {
        $.ajax({
            url: "addFavByUsernameAndExceptionId",
            type:"POST",
            data:{"id":id},
            success:function (res) {
                favoriteStatus(id)
                if(res == 1){
                    //收藏成功！刷新收藏
                }else if (res == -1 || res ==0) {
                    alert("您已经收藏过啦！")
                }else{
                    alert("请检查登录状态！")
                }
            }
        })
    }

    //取消收藏
    function favoriteCancel(id) {
        $.ajax({
            url: "deleteFavFromFavByUsernameAndExceptionId",
            type:"POST",
            data:{"id":id},
            success:function (res) {
                if(res == 1){
                    //删除成功！刷新收藏
                    favoriteStatus(id)
                }else{
                    alert("请检查登录状态！")
                }
            }
        })
    }
    /**
     * approve
     */
    //查询本文有多少赞
    function approveCount(id) {
        $.ajax({
            url: "findApproCountByExceptionId",
            type: "GET",
            data: {"id": id},
            success: function (res) {
                //初始化
                $(".ApproveBtn").html("❤");
                $(".ApproveBtn").append(res);
            }
        })
    }

    //检查赞赏状态
    function approveStatus(id) {
        $.ajax({
            url: "isAproByUsernameAndExceptionId",
            type: "GET",
            data: {"id": id},
            success: function (res) {
                if (res == 1) {
                    //已赞将按钮变成disabled
                    $(".ApproveBtn").attr("disabled", "true");
                }
            }
        })
        approveCount(id)
    }

//添加赞
    function addApprove(id) {
        $.ajax({
            url: "addAproByUsernameAndExceptionId",
            type: "POST",
            data:{"id":id},
            success: function (res) {
                if (res == 1) {
                    //更新赞数
                    approveCount(id);
                } else if(res ==-1 || res ==0){
                    alert("您已经赞过啦！~")

                }else{
                    //赞赏失败
                    alert("请检查登录状态！")
                }
            }

        })
    }

    /**
     * comments
     */
    //加载评论信息
    function loadingComments(id) {
        //初始化dom
        $(".comment_list").html("")
        $.ajax({
            url: "findComment",
            type:"GET",
            data:{"id":id},
            success: function (res) {
                var e = res;
                if(e.length == 0){
                    $(".comment_list").html("<div class='text-center bg-warning' style='font-weight: 800;'>本文还没有评论哦！~</div>")
                }else{
                    for (var i = 0; i < e.length; i++) {
                        $(".comment_list").append("<div class=\"col-xs-12 comment_body\"\n" +
                            "                 style=\"padding: 2%;background: white;border-radius: 10px;margin-top: 3%\">\n" +
                            "                #"+(i+1)+"楼\n" +
                            "                <div class=\"col-xs-12\">\n" +
                            "                    <span>发表人："+e[i].userId+"</span>\n" +
                            "                </div>\n" +
                            "                <div class=\"col-xs-12\" style=\"margin-top: 2%\">\n" +
                            "                    <span>"+e[i].content+"</span>\n" +
                            "                </div>\n" +
                            "                <hr>\n" +
                            "                <div class=\"col-xs-12 text-right\">\n" +
                            "                    <span>时间："+e[i].time+"</span>\n" +
                            "                </div>\n" +
                            "            </div>")
                    }
                }

            }
        })
    }

//添加评论
    function addComments(id) {
        if($("#commentContent").val().trim() == ""){
            $("#commentContent").focus();
            return;
        }else{
            $.ajax({
                url: "insertComment",
                type:"POST",
                data:{"id":id,"content":$("#commentContent").val()},
                success:function (res) {
                    if(res == 1){
                        //添加成功则刷新评论
                        loadingComments(id);
                        $("#commentContent").val("")
                    }else{
                        alert("请检查登录状态！")
                    }
                }
            })
        }

    }



    function newListLoading() {
        $(".load").show()
        $.ajax({
            url:"newListException",
            type:"GET",
            success:function (res) {
                var e = res;
                $(".newList ul").html("");
                for (var i = 0; i <e.length ; i++) {
                    $(".newList ul").append("<li><a style='cursor: pointer;width: 120px;overflow: hidden' onclick='toDetailPage("+e[i].id+")'>"+e[i].title+"</a>- - - -<span>"+e[i].createTime+"</span></li>")
                }
                $(".load").hide()
            }
        })
    }
    function myListLoading() {
        $(".load").show()
        $.ajax({
            url:"myListException",
            type:"GET",
            success:function (res) {
                var e = res;
                $(".myList ul").html("");
                for (var i = 0; i <e.length ; i++) {
                    $(".myList ul").append("<li><a style='cursor: pointer;width: 120px;overflow: hidden' onclick='toDetailPage("+e[i].id+")'>"+e[i].title+"</a>- - - -<span>"+e[i].createTime+"</span></li>")
                }
                $(".load").hide()
            }
        })
    }
    //收藏
    function myFavoriteLoading() {
        $(".favoriteList ul").html("");
        $.ajax({
            url: "findFavByUsername",
            type: "GET",
            success:function (res) {
                var e = res;
                if(e.length != 0){
                    //为空
                    for (var i = 0; i <e.length ; i++) {
                        $(".favoriteList ul").append("<li><a style='cursor: pointer;width: 120px;overflow: hidden' onclick='toDetailPage("+e[i].id+")'>"+e[i].title+"</a></li>")
                    }
                }
                else {
                    $(".favoriteList ul").html("<div class='text-center bg-warning' style='font-weight: 800;'>您还没有收藏文章哦！~</div>")
                }
            }
        });
    }
    //历史记录
    function historyLoading() {
        $(".historyList ul").html("");
        $.ajax({
            url: "findHistoryByUsername",
            type: "GET",
            success:function (res) {
                var e = res;
                if(e.length != 0){	//为空
                    for (var i = 0; i <e.length ; i++) {
                        $(".historyList ul").append("<li><a style='cursor: pointer;width: 120px;overflow: hidden' onclick='toDetailPage("+e[i].id+")'>"+e[i].title+"</a></li>")
                    }
                }
                else {
                    $(".historyList ul").html("<div class='text-center bg-warning' style='font-weight: 800;'>暂无历史记录！~</div>")
                }
            }
        });
    }


    function myInfo() {
        $(".load").show()
        $.ajax({
            url: "userInfo",
            type: "GET",
            success: function (res) {
                var e = res;
                $(".userInfo ul").html("")
                $(".userInfo ul").append("<li><strong style='font-weight: 800'>账号：</strong>"+e.username+"</li>\n")
                $(".userInfo ul").append("<li><strong style='font-weight: 800'>昵称：</strong>"+e.nickName+"</li>\n")
                $(".userInfo ul").append("<li><strong style='font-weight: 800'>性别：</strong>"+e.gender+"</li>\n")
                $(".userInfo ul").append("<li><strong style='font-weight: 800'>年龄：</strong>"+e.age+"</li>\n")
                $(".userInfo ul").append("<li><strong style='font-weight: 800'>邮箱：</strong>"+e.email+"</li>\n")
                $(".load").hide()
            }
        })
    }
    function updateUserInfo(){
        $("#myModal_enterUp").click(function () {
            enterUpdate();
        })
    }
    function enterUpdate() {
        var nickname = $("input[name='nickname']").val();
        var password = $("input[name='password']").val();
        var age = $(".age option:selected").val();
        var gender = $(".gender option:selected").val();
        if(nickname == "")
        {
            $("input[name='nickname']").focus();
            return;
        }else if(password == ""){
            $("input[name='password']").focus();
        }else{
            $.ajax({
                url:"userInfoUpdate",
                type:"POST",
                data:{"nickname":nickname,"password":hex_md5(password),"gender":gender,"age":age},
                success:function (res) {
                    if(res ==1){
                        //隐藏模态框
                        $("#cancelUp").click();
                        //刷新用户的信息
                        myInfo();
                        //更新DOM
                        $("input[name='nickname']").val("")
                        $("input[name='password']").val("")
                    }else{
                        alert("修改失败")
                    }
                }
            })
        }
    }
