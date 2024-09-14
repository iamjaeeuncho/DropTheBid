<template>
  <div>
    <div>
      <input v-model="message" @keyup.enter="sendMessage" placeholder="Type a message..."/>
      <button @click="sendMessage">Send</button>
    </div>
    <div v-for="msg in messages" :key="msg.chatMessageId">
      {{ msg.message }} ({{ msg.createdAt }})
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';

export default {
  data() {
    return {
      stompClient: null,
      message: '',
      messages: [],
    };
  },
  mounted() {
    this.connectWebSocket();
  },
  methods: {
    connectWebSocket() {
        const socket = new SockJS("http://localhost:8080/ws");
        this.stompClient = Stomp.over(socket);

        const token = localStorage.getItem('token'); // JWT 토큰 가져오기

        this.stompClient.connect({ Authorization: 'Bearer ' + token }, (frame) => {
            console.log("Connected: " + frame);

            this.stompClient.subscribe("/topic/messages", (message) => {
                const receivedMessage = JSON.parse(message.body);
                this.messages.push(receivedMessage);
            });
        }, (error) => {
            console.error("STOMP Error: " + error);
        });
    },
    sendMessage() {
      if (this.message.trim()) {
        const chatMessage = {
          chatMessageId: null, // 서버에서 생성된 ID로 대체될 것입니다
          chatRoomId: 1,      // 채팅방 ID (적절히 설정하세요)
          memberId: 1,        // 사용자 ID (적절히 설정하세요)
          message: this.message,
          createdAt: new Date().toISOString(),
        };

        this.stompClient.send("/app/send", {}, JSON.stringify(chatMessage));
        this.message = ''; // 메시지 전송 후 입력창 비우기
      }
    }
  }
};
</script>

<style scoped>
/* 스타일링을 여기에 추가할 수 있습니다 */
</style>
