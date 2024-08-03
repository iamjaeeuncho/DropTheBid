import axios from 'axios';
import store from '@/store';
import router from '@/router';

const axiosInstance = axios.create();

axiosInstance.interceptors.request.use(
    function (config) {
        const accessToken = localStorage.getItem('accessToken');

        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
            console.log(accessToken);
        }
        console.log(config);
        return config;
    },
    function (error) {
        console.log("11111");
        return Promise.reject(error);
    },
);

axiosInstance.interceptors.response.use(
    function (response) {
        return response;
    },

    function (error) {
        const originalRequest = error.config;

        if (error.response.status === 401) {
            return axiosInstance
                .post('/members/checks/refresh-token', {
                    withCredentials: true,
                })
                .then((result) => {
                    store.dispatch('member/login', { access_token: result.data });

                    return axiosInstance(originalRequest);
                })
                .catch(() => {
                    console.log("gdgd")
                    store.dispatch('member/logout');
                    if (router.currentRoute.path !== '/signin') {
                        router.push('/signin');
                    }
                });
        }
        return Promise.reject(error);
    },
);

export default axiosInstance;
