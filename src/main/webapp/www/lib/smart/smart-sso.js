var SmartSSO = {
    init: function(urls) {
        // 解决 IE8 不支持 CORS 的问题
        $.support.cors = true;
        // 解决 Ajax 重定向问题
        $.ajaxSetup({
            error: function(jqXHR, textStatus, errorThrown) {
                if (textStatus == 'error' && errorThrown == '') {
                    if (jqXHR.status == 0 || jqXHR.status == 12017) {
                        $.each(urls, function(i, url) {
                            var $iframe = $('<iframe>', {
                                src: url,
                                style: 'display: none'
                            });
                            $iframe.appendTo('body');
                            $iframe.load(function() {
                                if (i == urls.length - 1) {
                                    location.reload();
                                }
                            });
                        });
                    }
                }
            }
        });
    }
};