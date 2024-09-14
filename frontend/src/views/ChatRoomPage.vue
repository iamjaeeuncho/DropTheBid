<template>
  <div>
    <div v-for="msg in messages" :key="msg.chatMessageId">
      <strong>{{ msg.memberId }}:</strong> {{ msg.message }}
    </div>
    <input v-model="message" @keyup.enter="sendMessage" placeholder="Type your message" />
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

export default {
  data() {
    return {
      message: '',
      messages: [],
      stompClient: null
    };
  },
  mounted() {
    this.connect();
  },
  beforeDestroy() {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  },
  methods: {
    connect() {
      const socket = new SockJS('http://localhost:8080/ws');
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        onConnect: frame => {
          console.log('Connected: ' + frame);
          this.stompClient.subscribe('/topic/messages', message => {
            this.messages.push(JSON.parse(message.body));
          });
        }
      });
      this.stompClient.activate();
    },
    sendMessage() {
      if (this.message.trim()) {
        const chatMessage = {
          chatMessageId: null, // Your logic for ID generation
          chatRoomId: 1, // Set your chat room ID
          memberId: 1, // Set your member ID
          message: this.message,
          createdAt: new Date().toISOString()
        };
        this.stompClient.publish({
          destination: '/app/send',
          body: JSON.stringify(chatMessage)
        });
        this.message = '';
      }
    }
  }
};
</script>

<style scoped>
/* Add your styles here */
</style>
