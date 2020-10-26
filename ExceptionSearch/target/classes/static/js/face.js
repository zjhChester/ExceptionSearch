(function () {
    var input = document.querySelector('input');
    input.onchange = function () {
        lrz(this.files[0], {width: 600}, function (results) {
            // 你需要的数据都在这里，可以以字符串的形式传送base64给服务端转存为图片。
			$("#hidden").val(results.base64);
			$("#enter").css("visibility", "visible");
            // 以下为演示用内容
           report = document.querySelector('#report'),
            demo_report('原始图片', results.blob, results.origin.size);

            setTimeout(function () {
                demo_report('客户端预压的图片', results.base64, results.base64.length );
            }, 100);
        });
    };

    /**
     * 演示报告
     * @param title
     * @param src
     * @param size
     */
    function demo_report(title, src, size) {
        var img = $('#resImg'),
            li = document.createElement('li'),
            size = (size / 1024).toFixed(2) + 'KB';

        img.onload = function () {
            var content = '<ul>' +
                '<li>' + title + '（' + img.width + ' X ' + img.height + '）</li>' +
                '<li class="text-cyan">' + size + '</li>' +
                '</ul>';

            li.className = 'item';
            li.innerHTML = content;
            li.appendChild(img);
            document.querySelector('#report').appendChild(li);
        };

        img.src = src;
		$('#resImg').attr("src",src);
		var preview = document.querySelector('img');
		preview.hidden = "";
    }

    // 演示用服务器太慢，做个延缓加载
    window.onload = function () {
        input.style.display = 'block';
    }
})();