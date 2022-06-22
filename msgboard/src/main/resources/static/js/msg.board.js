const host = "http://127.0.0.1:8629/";

const loginModal = new Vue( {// 登录模态窗
    el: "#loginModal",
    data: {
        loginMsg: '',
        loginFail: false // 是否登录失败
    },
    methods: {
        login: function () { // 登录方法
            const uid = $('#loginUser').val()
            const pwd = $('#loginPwd').val()
            const remember = $('#rememberMe').is(':checked')
            const that = this
            const path = host + 'app/user/login.json'
            $.ajax({
                type: 'POST',
                xhrFields: {
                    withCredentials: true
                },
                url: path,
                data: {
                    userName: uid,
                    password: pwd,
                    rememberMe: remember
                },
                dataType: 'json',
                success: function (data, status, xhr) {
                    if (data.code == 200) {
                        userInfo.loginCheck = true
                        userInfo.userName = data.data.userName
                        userInfo.email = data.data.email
                        that.loginMsg = ''
                        $('#loginModal').modal('hide')
                        that.loginFail = false
                        $('#loginUser').val('')
                        $('#loginPwd').val('')
                    } else {
                        userInfo.loginCheck = false
                        userInfo.userName = ''
                        userInfo.email = ''
                        that.loginMsg = data.msg
                        that.loginFail = true
                    }
                }
            });
        }
    }
})
const userInfo = new Vue({ // 顶部用户信息
    el: '#user-info',
    data: {
        loginCheck: false, // 是否登录
        userName: '', // 用户名
        email: '' // 邮箱
    },
    methods: {
        checkLogin: function () {
            const that = this
            const path = host + 'app/user/checkLogin.json'
            $.ajax({
                type: 'POST',
                url: path,
                dataType: 'json',
                success: function (data) {
                    if (data.code == 200) {
                        that.loginCheck = true
                        that.userName = data.data.userName
                        that.email = data.data.email
                    } else {
                        that.loginCheck = false
                        that.userName = ''
                        that.email = ''
                    }
                }
            });
        },
        logout: function () {
            const that = this
            const path = host + 'app/user/logout.json'
            $.ajax({
                type: 'POST',
                url: path,
                dataType: 'json',
                success: function (data) {
                    if (data.code == 200) {
                        that.loginCheck = false
                        that.userName = ''
                        that.email = ''
                    } else {

                    }
                }
            });

        }
    },
});

const regModal = new Vue({ // 注册模态窗
    el: "#regModal",
    data: {
        regMsg: '',
        regFail: false
    },
    methods: {
        reg: function () {
            const uid = $('#regUser').val()
            const pwd = $('#regPwd').val()
            const email = $('#regEmail').val()
            const that = this
            const path = host + 'app/user/register.json'
            $.ajax({
                type: 'POST',
                url: path,
                data: {
                    userName: uid,
                    password: pwd,
                    email: email
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == 200) {
                        userInfo.loginCheck = true
                        userInfo.userName = data.data.userName
                        userInfo.email = data.data.email
                        that.regMsg = ''
                        $('#regModal').modal('hide')
                        that.regFail= false
                        $('#regUser').val('')
                        $('#regPwd').val('')
                        $('#regEmail').val('')
                    } else {
                        userInfo.loginCheck = false
                        userInfo.userName = ''
                        userInfo.email = ''
                        that.regMsg = data.msg
                        that.regFail = true
                    }
                }
            });
        }
    }
})

userInfo.checkLogin();

// 留言组件
Vue.component('single-msg', {
    props: ['msg'],
    data() {
        return {info: this.msg}
    },
    template: '<div class="row single-msg">' +
        '<div class="col-xs-12">' +
        '<div class="row">' +
        '<div class="col-xs-3 operator">' +
        '用户 {{ msg.userName }}' +
        '</div>' +
        '<div class="col-xs-6 op-time">' +
        '发表于 {{ msg.createTime }}' +
        '</div>' +
        '<div class="col-xs-3 reply">' +
        '<button class="btn btn-default btn-xs" @click="cclick">回复</button>' +
        '</div>' +
        '</div>' +
        '<div class="row">' +
        '<div class="col-xs-12 msg-content">' +
        '{{ msg.msg }}' +
        '</div>' +
        '</div>' +
        '<div class="row sub-msg">' +
        '<single-msg v-for="subMsg in msg.subMsgList" v-bind:msg="subMsg"></single-msg>' +
        '</div>' +
        '</div>' +
        '</div>',
    methods: {
        cclick: function() {
            this.$emit("replyclick", this.info)
        }
    },
    watch: {
        'msg': function (val) {
            this.info = val;
        }
    }


})



// 留言板模块
const msgBoard = new Vue({
    el: "#msg-board",
    data: {
        msgs: ''
    },
    methods: {
        getMsgList: function () {
            const that = this
            const path = host + 'app/msg/getMsgList.json'
            $.ajax({
                type: 'POST',
                url: path,

                dataType: 'json',
                success: function (data) {
                    if (data.code == 200) {
                        that.msgs = data.data
                    } else {

                    }
                }
            });
        },
        reply: function (msg) { // 打开回复窗口
            msgModal.hasError = false
            msgModal.msgError = ''
            msgModal.notFirstLayer = true
            msgModal.parentId = msg.id
            msgModal.msgTo = '回复: ' + msg.userName
            msgModal.wordCount = 0
            msgModal.msgContent = ''
            $('#msgModal').modal('show')
        }
    }
})

function cleanContent() { // 清空上次留言信息
    msgModal.hasError = false
    msgModal.msgError = ''
    msgModal.notFirstLayer = false
    msgModal.parentId = -1
    msgModal.msgTo = ''
    msgModal.wordCount = 0
    msgModal.msgContent = ''
}

const msgModal = new Vue({ // 留言模态窗
    el: '#msgModal',
    data: {
        hasError: false, // 内容是否违规
        msgError: '', // 违规内容
        notFirstLayer: true, // 是否不是顶级留言
        parentId: -1, // 上级留言 id
        msgTo: '', // 被评论人
        wordCount: 0, // 留言字数
        msgContent: '' // 留言内容
    },
    methods: {
      submitMsg: function() {
              const that = this
              const path = host + 'app/msg/submitMsg.json'
              $.ajax({
                  type: 'POST',
                  url: path,
                  xhrFields: {
                      withCredentials:true
                  },
                  crossDomain:true,
                  data: {
                      parentId: that.parentId,
                      msg: that.msgContent
                  },
                  dataType: 'json',
                  success: function (data) {
                      if (data.code == 200) {
                          $('#msgModal').modal('hide')
                          that.hasError = false
                          that.msgError = ''
                          that.notFirstLayer = true
                          that.parentId = -1
                          that.wordCount = 0
                          that.msgContent = ''
                          that.msgTo = ''
                          msgBoard.getMsgList()
                      } else {
                          that.msgError = data.msg
                          that.hasError = true
                      }
                  }
              });
      },
        checkCount: function() { // 校验留言字数
            let count = this.msgContent.length
            this.wordCount = count
            if (count > 200) {
                $('#leave-count').css('color', 'red')
            } else {
                $('#leave-count').css('color', 'black')
            }
        }
    }
})
msgBoard.getMsgList()

const mock_data = "[{\"msg\":\"23 阿斯顿发斯蒂芬\",\"createTime\":\"2022-03-07 19:30:49\",\"id\":49,\"subMsgList\":[{\"msg\":\"两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字两百个字\",\"createTime\":\"2022-03-07 19:38:56\",\"id\":54,\"subMsgList\":[],\"userName\":\"neko629\",\"parentId\":49},{\"msg\":\"asfd\",\"createTime\":\"2022-03-07 19:38:06\",\"id\":53,\"subMsgList\":[],\"userName\":\"neko629\",\"parentId\":49},{\"msg\":\"汇率接口了；啊看来是积分该口令迦拉克隆撒\",\"createTime\":\"2022-03-07 19:37:10\",\"id\":51,\"subMsgList\":[],\"userName\":\"neko629\",\"parentId\":49},{\"msg\":\"阿斯顿发斯蒂芬\",\"createTime\":\"2022-03-07 19:31:01\",\"id\":50,\"subMsgList\":[],\"userName\":\"neko629\",\"parentId\":49},{\"msg\":\"防守打法\",\"createTime\":\"2022-03-07 19:37:30\",\"id\":52,\"subMsgList\":[],\"userName\":\"neko629\",\"parentId\":49}],\"userName\":\"neko629\",\"parentId\":-1},{\"msg\":\"njAgVYBTQSFDJEmy1ZwxDnh0lDqF5N3BQMV2UIM8XJlfX12P5hZHqeYTrNNQGotbk6azHwJJeGKc2KDtSMPcK89LXVSN6dMmCh7c\",\"createTime\":\"2022-03-07 14:42:29\",\"id\":34,\"subMsgList\":[{\"msg\":\"aJKr3On2dEDry4vwAL9xGxCBpUPFoPNaLQPcyyRlUjUg8O9JV2Wlq69D6iVvcW3JqiPiGUVEXpnaNwIFTZKZJ2VdT3OkE7vTEtYb\",\"createTime\":\"2022-03-07 14:42:29\",\"id\":35,\"subMsgList\":[{\"msg\":\"Fv63LEToA6TV2jeWjEdobrGviziHQrDBqmoY1Jpv8yGcXqEDm8FcDfKUWxVLMErfBOAAi7Ym0yZhW9TMMGmKDmm9DWqnwCvZ6itS\",\"createTime\":\"2022-03-07 14:42:29\",\"id\":36,\"subMsgList\":[{\"msg\":\"cwKhMDP79QlmvTLZMR0wlVPEIK9YoFHxJIEEoNVf3otRlgc0VtJnTuwmeTzye3LJwLi1ITJPFKwoNegFizTrLkVfVg2kOYqqiAWG\",\"createTime\":\"2022-03-07 14:42:29\",\"id\":37,\"subMsgList\":[{\"msg\":\"zsZzPi1VHUcOs67D2oaJlcTxsIIcJpmit5RGHJ5aeGcdPJopBeqUd2CWA8OLX031W8HJsxeS2N7po4U62CX4fTNmF8mTGSfivAzF\",\"createTime\":\"2022-03-07 14:42:30\",\"id\":38,\"subMsgList\":[{\"msg\":\"bepPo5cTuM4GpOwe36bhE0oPKpDzfzeAa4ihpf9xVxhhzvuVnTNegQrgXUiDn5Gl3G1Q3UaxK38ydHwrYTOd6fz2bv86LVUwCqWA\",\"createTime\":\"2022-03-07 14:42:30\",\"id\":39,\"subMsgList\":[{\"msg\":\"Mn5yaP9yNLP6mBj4NOHm11HUjTfixGQj2LIPJRSRMFtjqcgtYYKpYP7iZaFXExbG7BWLX7DAtqhrFpof1hUnyavoUgQyN4SOiJHC\",\"createTime\":\"2022-03-07 14:42:30\",\"id\":40,\"subMsgList\":[{\"msg\":\"s4kkyPC0V0UWY1JEVvIJMvKokEMpLumc6dzPH9FIWubGeTrY6TqvOgzHTQ7O88dlDcSzXKmAmNMqjnH2kgckR4Ch4DfcTlpm1iU8\",\"createTime\":\"2022-03-07 14:42:30\",\"id\":41,\"subMsgList\":[{\"msg\":\"39m33jJSNB7HxHafNkes2uoDVa8cjtuSbc6YgeRGyG8nUsua2wNo9DSW5wUaOxEtDlJFGESK7EdQs4vnYTBEwk9kFyINZS2IRX72\",\"createTime\":\"2022-03-07 14:42:30\",\"id\":42,\"subMsgList\":[{\"msg\":\"5ORdQG18RSG3joP5PTYwjPbxJWPsc9MsDXVSDXP5yScySDpT2XfsYliRPuvaXnXbcUm8ApF9VN93STNsHBqJVdrTHqwHSYpLHqJf\",\"createTime\":\"2022-03-07 14:42:31\",\"id\":43,\"subMsgList\":[{\"msg\":\"YvRnUNqv10QouZKAgAsd8DsD8S54RsNJOQwMTWFYYt9mlTeABMH7rkoXxNh2BGKTgKWK9pxITsphM8stLovPnzBLLGYzCV26jTNu\",\"createTime\":\"2022-03-07 14:42:31\",\"id\":44,\"subMsgList\":[],\"userName\":\"neko629\",\"parentId\":43}],\"userName\":\"neko629\",\"parentId\":42}],\"userName\":\"neko629\",\"parentId\":41}],\"userName\":\"neko629\",\"parentId\":40}],\"userName\":\"neko629\",\"parentId\":39}],\"userName\":\"neko629\",\"parentId\":38}],\"userName\":\"neko629\",\"parentId\":37}],\"userName\":\"neko629\",\"parentId\":36}],\"userName\":\"neko629\",\"parentId\":35}],\"userName\":\"neko629\",\"parentId\":34}],\"userName\":\"neko629\",\"parentId\":-1},{\"msg\":\"33我回家贝纳颂的开发\",\"createTime\":\"2022-03-07 15:13:26\",\"id\":46,\"subMsgList\":[{\"msg\":\"asdfqwef   阿斯利康；就法拉克；地方\",\"createTime\":\"2022-03-07 19:27:43\",\"id\":48,\"subMsgList\":[],\"userName\":\"neko629\",\"parentId\":46},{\"msg\":\"sadfasdfasfsdafasdf\",\"createTime\":\"2022-03-07 19:27:20\",\"id\":47,\"subMsgList\":[],\"userName\":\"neko629\",\"parentId\":46}],\"userName\":\"neko629\",\"parentId\":-1},{\"msg\":\"WubfK7fQmcvUAUhsZNd20A2cnZOmM8i8wbXSoTJirBBWiTw14dyBoFVWIFNeBUxrsCzd3Izv94V3cDQotsUNMQxmiUasxOeJ7lKc\",\"createTime\":\"2022-03-07 14:02:06\",\"id\":1,\"subMsgList\":[{\"msg\":\"P0ucLpy7tPND9yQUUyeOcbWUmox4DGX9s6h9qUATHghCGWjSqSvHzA1mwo6jEbnA596DwBtbXRpdqk6roJZoHSP3GWEKsuXAvsKq\",\"createTime\":\"2022-03-07 14:02:06\",\"id\":2,\"subMsgList\":[{\"msg\":\"weHY9RQVkoLLq4F9kAs9RcxW0PXcW0vwa1wanhkKMRgHXalxOqlCE17AdJeAfg4joGuZGvl1X8ZKvce7OvaaowcBT4z45nSY6XoQ\",\"createTime\":\"2022-03-07 14:02:07\",\"id\":3,\"subMsgList\":[{\"msg\":\"tWQ4pwzAaUQAwlhJIvvwakTwxGVnRiIqTuL3x8PIoCt55eh0wCoNbptCFvYXdRJFuNnca405dZ6pc9T7qXmxoNMWEDlUQxrZln2L\",\"createTime\":\"2022-03-07 14:02:07\",\"id\":4,\"subMsgList\":[{\"msg\":\"LZcJ77KJW3IHdAJqfwBSDHjQEp5D1gLxMowOcLNwSwcT33R2ChKM9GLSdD3jscMhj7O2Xi64BHmt8UZCL4p1tbWjjZw3OhMebrpj\",\"createTime\":\"2022-03-07 14:02:07\",\"id\":5,\"subMsgList\":[{\"msg\":\"J2mxL4D1fydrEE9t65zzHz5KvvuhpXajJvSIxoH1MdFyxklNSyoN5a5q7jqoPcqVQA2Umd2e3iJ7GvtTK3MdHxadN8gwflyGTs5N\",\"createTime\":\"2022-03-07 14:02:07\",\"id\":6,\"subMsgList\":[{\"msg\":\"JZCrSa3O2pqPwcAYA0OhRvg5iaDWLsI557Z5tKNQbL3dlY68m6BTvblKoJ4Eh22OS6oPazA3tU41z8KYjALPWIk9ih9JEaUIBnja\",\"createTime\":\"2022-03-07 14:02:07\",\"id\":7,\"subMsgList\":[{\"msg\":\"TQgHv7XLfgNdIi2AhoET0ICG7gB79BGzazfuHTQwQS8Ys7Zy7ms22t5JjrJ59ZgJRKWmUQxOpOz8vqlMzhlMOfnG2Qc7ydqACr3Q\",\"createTime\":\"2022-03-07 14:02:08\",\"id\":8,\"subMsgList\":[{\"msg\":\"ZFImlgQbUSlIm1QVPDMNaHLwHy3fzSzdNglrWGvpysAiXVPYa9niZlPLHzuVVHZKPu0kwc0C14rwGhZDBBBdNWNVcqqnyOrqCdFS\",\"createTime\":\"2022-03-07 14:02:08\",\"id\":9,\"subMsgList\":[{\"msg\":\"eZavRUzLHyiX0m7k3VyaQlurXsfpTndj87H1jvJgnFu3the4KdwlnC5hmqEa35KkHckBYLefmiWJdccH5RyBMuLRAKvEPj9eFt6x\",\"createTime\":\"2022-03-07 14:02:08\",\"id\":10,\"subMsgList\":[{\"msg\":\"wDzMwTRy4xBRcitmQWtCJKq1VbajNab1bSZADRWAdjnS5fQ7nBJVNhgbQnPu3b7AUZudFCA7i8adZX6ccBTTRKTeaKWJnRe5JYqU\",\"createTime\":\"2022-03-07 14:02:08\",\"id\":11,\"subMsgList\":[],\"userName\":\"cCrQ4Z\",\"parentId\":10}],\"userName\":\"cCrQ4Z\",\"parentId\":9}],\"userName\":\"cCrQ4Z\",\"parentId\":8}],\"userName\":\"cCrQ4Z\",\"parentId\":7}],\"userName\":\"cCrQ4Z\",\"parentId\":6}],\"userName\":\"cCrQ4Z\",\"parentId\":5}],\"userName\":\"cCrQ4Z\",\"parentId\":4}],\"userName\":\"cCrQ4Z\",\"parentId\":3}],\"userName\":\"cCrQ4Z\",\"parentId\":2}],\"userName\":\"cCrQ4Z\",\"parentId\":1}],\"userName\":\"cCrQ4Z\",\"parentId\":-1},{\"msg\":\"yicRpDceo9xmEaAy3Cq52lI8RmdO2j7HVeGggnqrCFSGs7QD6WM1LOX7XuPlpeLacna59FAG8peUcE8x6tmva59oLvgotnFUvoAQ\",\"createTime\":\"2022-03-07 14:03:32\",\"id\":23,\"subMsgList\":[{\"msg\":\"sDqE6cevG9kGoCDl8AslAmAQWgda28SMB5BfshznfiNjUTEtXO61FLt7BLZqOkfjiKcFmU6hzl4qfiNPJpxNbCD6bSu0MExovZoa\",\"createTime\":\"2022-03-07 14:03:33\",\"id\":24,\"subMsgList\":[{\"msg\":\"boIcXhFz0GSCraRi4vHDZA5z8gGSSuQOWX24gp1AI5qJLik1r0ys2bDiObR5wjh5PfKprrG4JzBj722VhONeTekvBeHS7XG5YudT\",\"createTime\":\"2022-03-07 14:03:33\",\"id\":25,\"subMsgList\":[{\"msg\":\"DKqhEjV7lqTWeraTszZO3xau8I4qiaXBM675gH8yBPYYrtTlvIS1DsyoU0pSfe04SEGsVUCkSRBvBzp8wCStCrRNXpdhV0Hec31N\",\"createTime\":\"2022-03-07 14:03:33\",\"id\":26,\"subMsgList\":[{\"msg\":\"VtDCykW4ZCZRCSvf8pHBLO0PVh2kDxrZC5vc4aXBgtZfd1d9y4dB6puKNicOYmDsu5DrWC3eUYed70AIsCDK6HphWlkTmv4IvnT7\",\"createTime\":\"2022-03-07 14:03:33\",\"id\":27,\"subMsgList\":[{\"msg\":\"pJ7r8bAJFM390JPx1gwX92j1iOytxC3eOBcuCjEQ9mcRR8N41YhH8VO6MdvNCsOLgnvdtt3JZghjw0lXzQdVPRaADG2nOS6UbkGB\",\"createTime\":\"2022-03-07 14:03:33\",\"id\":28,\"subMsgList\":[{\"msg\":\"xF8rmnv2RZ25NOFUk8jPskK3RMMKNoIkZUI456l4X5BDag46lRHEawzpjbbPC8BrMEXZvSbFQgzydjdVKgbWQsuLKK6LZm3ht77b\",\"createTime\":\"2022-03-07 14:03:34\",\"id\":29,\"subMsgList\":[{\"msg\":\"PPQqbvCqE4P8jAcFd2psofEIxAd4dc59yr6bF6SURgc0Zyh5grTCfFYJQM5dKiPIaui0VBM9IYMHOzAVngS4413olhIzCYDVdAYN\",\"createTime\":\"2022-03-07 14:03:34\",\"id\":30,\"subMsgList\":[{\"msg\":\"xyWJvwp0TofPSxwR8Lrjj9auf5cFlu8x3gtoq0E5dWiFVWuXTPvv7jFF1WRTPEKY7lRxvGZmhZIA9ziu9MRj2G3JtpHFYYskKvZF\",\"createTime\":\"2022-03-07 14:03:34\",\"id\":31,\"subMsgList\":[{\"msg\":\"QRTHkUj4aPtqQyPDuaBFzm9PaNwo4xVIrp9y6wjqmJmPNIgHmv9YzOus6Hlz6aMC2V90TSLDH7FYwVXzIFYvE5pABy2Up5bpVDHE\",\"createTime\":\"2022-03-07 14:03:34\",\"id\":32,\"subMsgList\":[{\"msg\":\"VbJ6CsKuEWeqtWMDhH3bgK13gLpZIwl3JladuBAZZ4zGw2kC5TWipVF8JbHmchN3ExVpKGj8VvapoX2Q2si7NcuitSqykWtisF20\",\"createTime\":\"2022-03-07 14:03:34\",\"id\":33,\"subMsgList\":[],\"userName\":\"cCrQ4Z\",\"parentId\":32}],\"userName\":\"cCrQ4Z\",\"parentId\":31}],\"userName\":\"cCrQ4Z\",\"parentId\":30}],\"userName\":\"cCrQ4Z\",\"parentId\":29}],\"userName\":\"cCrQ4Z\",\"parentId\":28}],\"userName\":\"cCrQ4Z\",\"parentId\":27}],\"userName\":\"cCrQ4Z\",\"parentId\":26}],\"userName\":\"cCrQ4Z\",\"parentId\":25}],\"userName\":\"cCrQ4Z\",\"parentId\":24}],\"userName\":\"cCrQ4Z\",\"parentId\":23}],\"userName\":\"cCrQ4Z\",\"parentId\":-1},{\"msg\":\"DaWT4GINcOIOCleYCJW2q7I6UGHhbF5BmgBrrE5KjcPXCSeBOp3iLwMC0RAY0h44Q9J5V57hT2TKm24yBAfQsVGWkklTo9b4w3oa\",\"createTime\":\"2022-03-07 14:02:59\",\"id\":12,\"subMsgList\":[{\"msg\":\"Qs691B0IYTZDCgOzxOX7pxz7oWihqTvc4kHNT2kICNCzo2H6msKpmEDAyIWsBxm9Igw12HeLJj7hXkc1AJwR314AQ8ilLHlvNYcI\",\"createTime\":\"2022-03-07 14:02:59\",\"id\":13,\"subMsgList\":[{\"msg\":\"ek5fkmqiC0JV5kv3JyY2pRv9D6kMCJMt5vgfadb4hIT4WC3vEl3DKjKmF0hQ3uSAJlTymZyu7IYAICXLRYxsxS44IlRgGQZszXmM\",\"createTime\":\"2022-03-07 14:02:59\",\"id\":14,\"subMsgList\":[{\"msg\":\"LriAepzRSZG6l2lPr3oPRXwLYQffk9wW417uw6eDzquV5KPoCXkvLTOGwks1WjFO8pNIU7IkI0VPLyRyxxNd1b6mOzlIvRNE7Lx6\",\"createTime\":\"2022-03-07 14:03:00\",\"id\":15,\"subMsgList\":[{\"msg\":\"VM8tr1Z4g0cedoEPqbL9ALWGmzxbPyKjfzEQTlXH7O0mfHh2gPjyfZlFE8QM6WWRtdLoVOShxdidlgeUrTPDivo8hHUz9XlWZ7mn\",\"createTime\":\"2022-03-07 14:03:00\",\"id\":16,\"subMsgList\":[{\"msg\":\"trLGJT2FKmG3EkCTnmpLtponvJYYYJU7bFu2JzlFQbPHxpliYn9dx6RSf3Mk5Ku9O3eQ6UyJPQNh1BbtMvLvJaBFSviEKOkOxVCL\",\"createTime\":\"2022-03-07 14:03:00\",\"id\":17,\"subMsgList\":[{\"msg\":\"R1wjYQOlmtEXMGfOFpIOQ11345CK7Uvp5fSHnW6O04M7T0vO5ZcfXGzEPCIqaVSVoMRA3WO70TpxBJoFUF5RWS3UmmGGr4AQCMdm\",\"createTime\":\"2022-03-07 14:03:00\",\"id\":18,\"subMsgList\":[{\"msg\":\"C8YazAVQ144fRYgu8lujfxWQWu90JOIPhUxAs5Qg9MSpC6yJvtG439V6plobsyBNHKNDG2YymBbzcEP2m9CsFvPkIm5tmrRopM7L\",\"createTime\":\"2022-03-07 14:03:00\",\"id\":19,\"subMsgList\":[{\"msg\":\"ODN70DEAx77QpXXLW8ZTN9NMN7wYQZKeRVPImeSo1xvBHZF69mIAyn6QvGly8MaMkjHxb0W4gfclShuBbZbw0KGtK6qgQNrPBzzZ\",\"createTime\":\"2022-03-07 14:03:01\",\"id\":20,\"subMsgList\":[{\"msg\":\"6HfwFGlqcQKD5iFo8SH4W4bptIYurnoJtfwcU9as8ZAxmY77gx9RYOTi5vfYShBEUNUYc7KaFkdYpZOnsmjrgZ1puzDaurYDett2\",\"createTime\":\"2022-03-07 14:03:01\",\"id\":21,\"subMsgList\":[{\"msg\":\"RnQsbbYdeHR38lXFtqlibNB9wtGC8AGMCQCSAtYuEj73dHXGmqCZ4NFqnDnFn3bvIywMF4v5gGMR9Ow8iuQCzNAJ3oSZCyDZ5RQQ\",\"createTime\":\"2022-03-07 14:03:01\",\"id\":22,\"subMsgList\":[],\"userName\":\"cCrQ4Z\",\"parentId\":21}],\"userName\":\"cCrQ4Z\",\"parentId\":20}],\"userName\":\"cCrQ4Z\",\"parentId\":19}],\"userName\":\"cCrQ4Z\",\"parentId\":18}],\"userName\":\"cCrQ4Z\",\"parentId\":17}],\"userName\":\"cCrQ4Z\",\"parentId\":16}],\"userName\":\"cCrQ4Z\",\"parentId\":15}],\"userName\":\"cCrQ4Z\",\"parentId\":14}],\"userName\":\"cCrQ4Z\",\"parentId\":13}],\"userName\":\"cCrQ4Z\",\"parentId\":12}],\"userName\":\"cCrQ4Z\",\"parentId\":-1},{\"msg\":\"阿斯顿发送到发斯蒂芬\",\"createTime\":\"2022-03-07 15:11:37\",\"id\":45,\"subMsgList\":[],\"userName\":\"neko629\",\"parentId\":-1}]"






