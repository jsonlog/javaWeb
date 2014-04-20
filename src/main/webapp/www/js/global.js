/* 全局变量 */
var BASE = '/smart4j-bootstrap'; // Context Path（若以 ROOT 发布，则为空字符串）

$(function() {
    // 扩展 jQuery 函数
    $.extend($, {
        render: function(template, data) {
            return template.replace(/\{([\w\.]*)\}/g, function(str, key) {
                var keys = key.split('.');
                var value = data[keys.shift()];
                for (var i = 0, l = keys.length; i < l; i++) {
                    value = value[keys[i]];
                }
                return (typeof value !== 'undefined' && value !== null) ? value : '';
            });
        },
        i18n: function() {
            var args = arguments;
            var code = args[0];
            var text = window['I18N'][code];
            if (text) {
                if (args.length > 0) {
                    text = text.replace(/\{(\d+)\}/g, function(m, i) {
                        return args[parseInt(i) + 1];
                    });
                }
                return text;
            } else {
                return code;
            }
        }
    });

    // 忽略空链接
    $('a[href="#"]').click(function() {
        return false;
    });

    // 全局 AJAX 设置
    $.ajaxSetup({
        cache: true,
        error: function(jqXHR, textStatus, errorThrown) {
            switch (jqXHR.status) {
                case 403:
                    document.write('');
                    location.href = BASE + '/';
                    break;
                case 503:
                    alert(errorThrown);
                    break;
            }
        }
    });

    // 切换系统语言
    $('#language').find('a').click(function() {
        var language = $(this).data('value');
        $.cookie('cookie_language', language, {expires: 365, path: '/'});
        location.reload();
    });

    // 绑定注销事件
    $('#logout').click(function() {
        if (confirm($.i18n('common.logout_confirm'))) {
            $.ajax({
                type: 'get',
                url: BASE + '/logout',
                success: function(result) {
                    if (result.success) {
                        location.href = BASE + '/';
                    }
                }
            });
        }
    });
});