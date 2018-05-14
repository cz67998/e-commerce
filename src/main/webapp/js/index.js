$(function () {
    $('#pp').pagination({
        total: 100,
        pageSize: 10,
    });

    $('#search').click(function () {
        var username = $.trim($('#username').val());
        var password = $.trim($('#password').val());
        if (username == "") {
            $.messager.alert('提示', "请输入用户名！", 'info');
            return;
        }
        if (password == "") {
            $.messager.alert('提示', "请输入密码！", 'info');
            return;
        }
        var data = {};
        data.username = username;
        data.password = password;
        alert(JSON.stringify(data));
    });

    $('#reset').click(function () {
        $('#username').val('');
        $('#password').val('');
        $('#start_date').datebox({
            required: false
        });
        $('#end_date').datebox({
            required: false
        });
    });
});
