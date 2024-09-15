<template>
  <div>
    <h2><center>채팅하기</center></h2>
    <div class="chat-container">
      <div class="messages">
        <div v-for="msg in messages" :key="msg.chatMessageId" class="message">
          <strong class="member-id">{{ msg.memberId }}:</strong> {{ msg.message }}
        </div>
      </div>
      <div class="input-container">
        <input
          v-model="message"
          @keyup.enter="sendMessage"
          placeholder="Type your message"
          class="message-input"
        />
        <button @click="sendMessage" class="send-button">Send</button>
      </div>
    </div>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import axiosInstance from '../utils/axiosinstance';

// 회원 정보 로드
const loadMemberInfo = async () => {
  try {
    const res = await axiosInstance.get('/members/info');
    return res.data;
  } catch (error) {
    console.error('Failed to load member info:', error);
    throw error;
  }
};

// 채팅방 생성 또는 참가
const createChatRoom = async (auctionId, memberId) => {
  try {
    const res = await axiosInstance.post('/room/join', {
      auctionId,
      memberId
    });
    return res.data;
  } catch (error) {
    console.error('Failed to create chat room:', error);
    throw error;
  }
};

export default {
  data() {
    return {
      message: '',
      messages: [],
      stompClient: null,
      chatRoomId: null,
      memberId: null,
    };
  },
  async mounted() {
    try {
      await this.loadInfo();
      await this.createOrJoinChatRoom();
      await this.loadMessages(); // 추가된 부분
      this.connect();
    } catch (error) {
      console.error('Error during mounting:', error);
    }
  },
  beforeDestroy() {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  },
  methods: {
    async loadInfo() {
      try {
        const memberInfo = await loadMemberInfo();
        this.memberId = memberInfo.memberId;
      } catch (error) {
        console.error('Failed to load member info:', error);
      }
    },
    async createOrJoinChatRoom() {
      try {
        const auctionId = this.$route.params.id;
        const chatRoom = await createChatRoom(auctionId, this.memberId);
        this.chatRoomId = chatRoom.chatRoomId;
      } catch (error) {
        console.error('Failed to create or join chat room:', error);
      }
    },
    async loadMessages() {
      try {
        const response = await axiosInstance.get(`/room/${this.chatRoomId}/messages`);
        this.messages = response.data;
      } catch (error) {
        console.error('Failed to load messages:', error);
      }
    },
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
          chatMessageId: null,
          chatRoomId: this.chatRoomId,
          memberId: this.memberId,
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
.chat-container {
  display: flex;
  flex-direction: column;
  height: 600px;
  max-width: 800px;
  margin: 0 auto;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  border-bottom: 1px solid #ddd;
}

.message {
  margin-bottom: 10px;
  padding: 5px;
  border-radius: 4px;
  background-color: #f1f1f1;
}

.member-id {
  color: #007bff;
  font-weight: bold;
}

.input-container {
  display: flex;
  padding: 10px;
  border-top: 1px solid #ddd;
  background: #fafafa;
  align-items: center;
}

.message-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  margin-right: 10px;
}

.message-input:focus {
  border-color: #007bff;
  outline: none;
}

.send-button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: #fff;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.send-button:hover {
  background-color: #0056b3;
}

.send-button:active {
  background-color: #004494;
}
</style>
