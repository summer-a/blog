window.live2d_settings = Array();
// 后端接口
live2d_settings['modelAPI']             = '//live2d.fghrsh.net/api/';   // 自建 API 修改这里
live2d_settings['tipsMessage']          = 'waifu-tips.json';            // 同目录下可省略路径
live2d_settings['hitokotoAPI']          = 'lwl12.com';                  // 一言 API，可选 'lwl12.com', 'hitokoto.cn', 'jinrishici.com'(古诗词)

// 默认模型
live2d_settings['modelId']              = 1;            // 默认模型 ID，可在 F12 控制台找到
live2d_settings['modelTexturesId']      = 53;           // 默认材质 ID，可在 F12 控制台找到

// 工具栏设置
live2d_settings['showToolMenu']         = true;         // 显示 工具栏          ，可选 true(真), false(假)
live2d_settings['canCloseLive2d']       = true;         // 显示 关闭看板娘  按钮，可选 true(真), false(假)
live2d_settings['canSwitchModel']       = true;         // 显示 模型切换    按钮，可选 true(真), false(假)
live2d_settings['canSwitchTextures']    = true;         // 显示 材质切换    按钮，可选 true(真), false(假)
live2d_settings['canSwitchHitokoto']    = true;         // 显示 一言切换    按钮，可选 true(真), false(假)
live2d_settings['canTakeScreenshot']    = true;         // 显示 看板娘截图  按钮，可选 true(真), false(假)
live2d_settings['canTurnToHomePage']    = true;         // 显示 返回首页    按钮，可选 true(真), false(假)
live2d_settings['canTurnToAboutPage']   = true;         // 显示 跳转关于页  按钮，可选 true(真), false(假)

// 模型切换模式
live2d_settings['modelStorage']         = true;         // 记录 ID (刷新后恢复)，可选 true(真), false(假)
live2d_settings['modelRandMode']        = 'switch';     // 模型切换，可选 'rand'(随机), 'switch'(顺序)
live2d_settings['modelTexturesRandMode']= 'rand';       // 材质切换，可选 'rand'(随机), 'switch'(顺序)

// 二维码
var qrcode                              = false;


function renderTip(template, context) {
    var tokenReg = /(\\)?\{([^\{\}\\]+)(\\)?\}/g;
    return template.replace(tokenReg, function (word, slash1, token, slash2) {
        if (slash1 || slash2) {
            return word.replace('\\', '');
        }
        var variables = token.replace(/\s/g, '').split('.');
        var currentObject = context;
        var i, length, variable;
        for (i = 0, length = variables.length; i < length; ++i) {
            variable = variables[i];
            currentObject = currentObject[variable];
            if (currentObject === undefined || currentObject === null) return '';
        }
        return currentObject;
    });
}

function loadRandTextures() {
    var modelId = modelStorageGetItem('modelId');
        modelId = modelId == null ? 1 : modelId;
    var modelTexturesId = modelStorageGetItem('modelTexturesId');
    var modelTexturesRandMode = live2d_settings.modelTexturesRandMode;

    $.ajax({
        cache: modelTexturesRandMode == 'switch' ? true : false,
        url: live2d_settings.modelAPI+modelTexturesRandMode+'_textures/?id='+modelId+'-'+modelTexturesId,
        dataType: "json",
        success: function(result) {
            /*if (result.textures['id'] == 1 && (modelTexturesId == 1 || modelTexturesId == 0))
                showMessage("哎呀！更换失败了！", 3000, true);
            else showMessage("更换成功！", 3000, true);*/
            showMessage("我的新衣服好看吗？", 3000, true);
            loadModel(modelId, result.textures['id']);
        }
    });
}

function modelStorageGetItem(key) { return live2d_settings.modelStorage ? localStorage.getItem(key) : sessionStorage.getItem(key); }

function loadModel(modelId, modelTexturesId) {
    if (modelId == null || modelTexturesId == null) {
        return ;
    }
    if (live2d_settings.modelStorage) {
        localStorage.setItem('modelId', modelId);
        localStorage.setItem('modelTexturesId', modelTexturesId);
    }

    loadlive2d('live2d', live2d_settings.modelAPI+'get/?id='+modelId+'-'+modelTexturesId, (live2d_settings.showF12Status ? console.log('[Status]','live2d','模型',modelId+'-'+modelTexturesId,'加载完成'):null));
}

String.prototype.renderTip = function (context) {
    return renderTip(this, context);
};

var re = /x/;
re.toString = function() {
    showMessage('哈哈，你打开了控制台，是想要看看我的秘密吗？', 5000);
    return '';
};

/*$(document).on('copy', function (){
    showMessage('你都复制了些什么呀，转载要记得加上出处哦~~', 5000);
});*/

$('.waifu-tool .fa-home').click(function(){
    window.location = '/'
});

$('.waifu-tool .fa-mobile').click(function() {
    console.log(location.href)
    // $(".message").animate({top:'-250px'},100)
    qrcode = true;
    showMessage('扫描下面的二维码就可以在手机上浏览了哦！', 20000);
    $(".message").css("top", "-150px")

    var qrcode = new QRCode(document.getElementsByClassName("message")[0], {
        width : 128,
        height : 128,
        colorDark : "#5d71ff",
        colorLight : "#ffffff",
        correctLevel : QRCode.CorrectLevel.H
    });
    qrcode.makeCode(location.href)
})

$('.waifu-tool .fa-music').click(function () {
    // 显示界面
    if (!fold) {
        $(".jp-video").animate({
            left: 0
        }, 350, function() {
            slideOut($("#btnfold"));
        });
    }
    // var playList = myPlaylist.playlist;
    // 随机播放
    myPlaylist.play(Math.floor(Math.random() * myPlaylist.playlist.length));

    updateTitle();
})


$('.waifu-tool .fa-street-view').click(function() {
    // 获取在线材质
    loadRandTextures()
})


$('.waifu-tool .fa-camera').click(function(){
    showMessage('照好了嘛，是不是很可爱呢？',8000);
    window.Live2D.captureName = '照片.png';
    window.Live2D.captureFrame = true;
});

$('.waifu-tool .fa-close').click(function(){

    localStorage.setItem('waifu-dsiplay','none');
    localStorage.setItem('live2d-status','hide');

    showMessage('愿你有一天能与重要的人重逢',2000);
    $('#landlord').animate({bottom:'-400px'},50)
    window.setTimeout(function(){$('#landlord').css("opacity", 0);},300);
});

function initTips(){
    $.ajax({
        cache: true,
        url: '/plugin/live2d/message.json', // 配置地址
        dataType: "json",
        success: function (result){
            $.each(result.mouseover, function (index, tips){
                $(tips.selector).mouseover(function (){
                    var text = tips.text;
                    if(Array.isArray(tips.text)) text = tips.text[Math.floor(Math.random() * tips.text.length + 1)-1];
                    text = text.renderTip({text: $(this).text()});
                    showMessage(text, 3000);
                });
            });
            $.each(result.click, function (index, tips){
                $(tips.selector).click(function (){
                    var text = tips.text;
                    if(Array.isArray(tips.text)) text = tips.text[Math.floor(Math.random() * tips.text.length + 1)-1];
                    text = text.renderTip({text: $(this).text()});
                    showMessage(text, 3000);
                });
            });
        }
    });
}
initTips();

(function (){
    var text;
    if(document.referrer !== ''){
        var referrer = document.createElement('a');
        referrer.href = document.referrer;
        text = '嗨！来自 <span style="color:#0099cc;">' + referrer.hostname + '</span> 的朋友！';
        var domain = referrer.hostname.split('.')[1];
        if (domain == 'baidu') {
            text = '嗨！ 来自 百度搜索 的朋友！<br>欢迎访问<span style="color:#0099cc;">「 ' + document.title.split(' - ')[0] + ' 」</span>';
        }else if (domain == 'so') {
            text = '嗨！ 来自 360搜索 的朋友！<br>欢迎访问<span style="color:#0099cc;">「 ' + document.title.split(' - ')[0] + ' 」</span>';
        }else if (domain == 'google') {
            text = '嗨！ 来自 谷歌搜索 的朋友！<br>欢迎访问<span style="color:#0099cc;">「 ' + document.title.split(' - ')[0] + ' 」</span>';
        }
    }else {
        if (window.location.href == (location.origin + '/')) { //主页URL判断，需要斜杠结尾
            var now = (new Date()).getHours();
            if (now > 23 || now <= 5) {
                text = '你是夜猫子呀？这么晚还不睡觉，明天起的来嘛？';
            } else if (now > 5 && now <= 7) {
                text = '早上好！一日之计在于晨，美好的一天就要开始了！';
            } else if (now > 7 && now <= 11) {
                text = '上午好！工作顺利嘛，不要久坐，多起来走动走动哦！';
            } else if (now > 11 && now <= 14) {
                text = '中午了，工作了一个上午，现在是午餐时间！';
            } else if (now > 14 && now <= 17) {
                text = '午后很容易犯困呢，今天的运动目标完成了吗？';
            } else if (now > 17 && now <= 19) {
                text = '傍晚了！窗外夕阳的景色很美丽呢，最美不过夕阳红~~';
            } else if (now > 19 && now <= 21) {
                text = '晚上好，今天过得怎么样？';
            } else if (now > 21 && now <= 23) {
                text = '已经这么晚了呀，早点休息吧，晚安~~';
            } else {
                text = '嗨~ 快来逗我玩吧！';
            }
        }else {
            text = '欢迎阅读<span style="color:#0099cc;">「 ' + document.title.split(' - ')[0] + ' 」</span>';
        }
    }
    showMessage(text, 12000);
})();

window.setInterval(showHitokoto,30000);

function showHitokoto(){
    $.getJSON('https://v1.hitokoto.cn/',function(result){
        showMessage(result.hitokoto, 5000);
    });
}

function showMessage(text, timeout){
    if (!qrcode && $(".message")[0].offsetTop < -40) {
        $(".message").css("top", "-40px");
        qrcode = false;
    }
    if(Array.isArray(text)) text = text[Math.floor(Math.random() * text.length + 1)-1];
    //console.log('showMessage', text);
    $('.message').stop();
    $('.message').html(text).fadeTo(200, 1);
    if (timeout === null) timeout = 5000;
    hideMessage(timeout);
}

function hideMessage(timeout){
    $('.message').stop().css('opacity',1);
    if (timeout === null) timeout = 5000;
    $('.message').delay(timeout).fadeTo(200, 0);
}

function initLive2d (){
    $('.hide-button').fadeOut(0).on('click', () => {
        $('#landlord').css('display', 'none')
    })
    $('#landlord').hover(() => {
        $('.hide-button').fadeIn(600)
    }, () => {
        $('.hide-button').fadeOut(600)
    })
}

var status = localStorage.getItem("live2d-status");
if (status === "hide") {
    $('#landlord').animate({bottom:'-400px'},50)
    window.setTimeout(function(){$('#landlord').css("opacity", 0);},300);
} else {
    $('#landlord').animate({bottom: '0px'}, 50)
    window.setTimeout(function () {
        $('#landlord').css("opacity", 1);
    }, 300);
}
// 初始化
initLive2d ();
