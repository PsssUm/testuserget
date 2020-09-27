# testuserget
Во фрагменте DocsFragment найдешь код, который вызывает метод loadUser
private fun loadUser(){
        L.d("loadUser")
        VK.execute(VKUsersGet(context!!), object :
            VKApiCallback<VKUser> {
            override fun success(result: VKUser) {
                L.d("VKUser name = " + result.first_name)
            }

            override fun fail(error: VKApiExecutionException) {
                swipeRefresh.isRefreshing = false
                L.d("VKApiExecutionException = " + error.errorMsg)
            }
        })
    }
этот метод получается инфу о юзере из ВК.
Сам код получения инфы о юзере найдешь в папке requests в классе VKUsersGet
Этот метод возвращает объект VKUser - это просто модель, она описана в папке Models(модель можешь и сам создать с нужными данными)
