import HomePage from '@/views/HomePage.vue';
import SignInPage from '@/views/SignInPage.vue';
import SignUpPage from '@/views/SignUpPage.vue';
import MyPage from '@/views/MyPage.vue';
import SearchResults from '@/views/SearchResults.vue';

import RegisterAuctionPage from '@/views/RegisterAuctionPage.vue';
import ModifyAuctionPage from '@/views/ModifyAuctionPage.vue';
import PaymentPage from '@/views/PaymentPage.vue';
import PaymentStatusPage from '@/views/PaymentStatusPage.vue';
import AuctionDetailPage from '@/views/AuctionDetailPage.vue';

import ChatRoomPage from '@/views/ChatRoomPage.vue';
import ChatListPage from '@/views/ChatListPage.vue';

export default [
    {
        path: '/',
        name: 'HomePage',
        component: HomePage,
    },
    {
        path: '/auction/:id',
        name: 'AuctionDetailPage',
        component: AuctionDetailPage,
    },
    {
        path: '/auction',
        name: 'RegisterAuctionPage',
        component: RegisterAuctionPage,
    },
    {
        path: '/auction/:id/modify',
        name: 'ModifyAuctionPage',
        component: ModifyAuctionPage,
    },
    {
        path: '/auctions/:id/payment',
        name: 'PaymentPage',
        component: PaymentPage,
    },
    {
        path: '/auctions/:id/paymentstatus',
        name: 'PaymentStatusPage',
        component: PaymentStatusPage,
    },
    {
        path: '/chat',
        name: 'ChatListPage',
        component: ChatListPage,
    },
    {
        path: '/chat/:id/:memberId',
        name: 'ChatRoomPage',
        component: ChatRoomPage,
    },
    {
        path: '/signin',
        name: 'SignInPage',
        component: SignInPage,
    },
    {
        path: '/signup',
        name: 'SignUpPage',
        component: SignUpPage,
    },
    {
        path: '/my-page',
        name: 'MyPage',
        component: MyPage,
    },
    {
        path: '/search',
        name: 'SearchResults',
        component: SearchResults,
    },
];
