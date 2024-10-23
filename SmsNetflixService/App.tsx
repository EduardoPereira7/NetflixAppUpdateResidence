import axios from 'axios';
import React, {useEffect, useState} from 'react';
import {Alert, PermissionsAndroid, Platform, Text, View} from 'react-native';
import SmsListener from 'react-native-android-sms-listener';

interface SMSMessage {
  body: string;
  originatingAddress: string;
}

const App = () => {
  const [messages, setMessages] = useState<string[]>([]);

  const requestSmsPermissions = async () => {
    if (Platform.OS === 'android') {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.RECEIVE_SMS,
        {
          title: 'SMS Permission',
          message:
            'This app needs access to your SMS messages to function properly.',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );
      if (granted !== PermissionsAndroid.RESULTS.GRANTED) {
        Alert.alert(
          'Permission Denied',
          'SMS permission is required to receive messages.',
        );
      }
    }
  };

  useEffect(() => {
    requestSmsPermissions();

    const subscription = SmsListener.addListener((message: SMSMessage) => {
      const {body, originatingAddress} = message;

      // Verifica se a mensagem contém "Netflix" e "residência", ou se é do número específico
      if (
        (body.includes('Netflix') && body.includes('residência')) ||
        originatingAddress === '+1 909-324-5063'
      ) {
        setMessages(prevMessages => [...prevMessages, body]);
        console.log('Received message:', body);
        sendToEndpoint(body);
      }
    });

    return () => {
      // Limpa o listener ao desmontar o componente
      subscription.remove();
    };
  }, []);

  const sendToEndpoint = async (message: string) => {
    try {
      const response = await axios.patch(
        'https://cxlnqtbmtvkzscowleil.supabase.co/rest/v1/message?id=eq.ca2dd11f-f6a3-4e05-b6b9-c40057b8724f',
        {content: message},
        {
          headers: {
            apikey:
              'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN4bG5xdGJtdHZrenNjb3dsZWlsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjkwOTk3MjcsImV4cCI6MjA0NDY3NTcyN30.qXE_dZ-Vp3EgE0Dvnf69U4Od7tvYmdkRjH_bbE86SxE',
            'Content-Type': 'application/json',
          },
        },
      );
      Alert.alert('Success', 'Message sent to the endpoint!');
    } catch (error) {
      Alert.alert('Error', 'Failed to send message');
      console.error(error);
    }
  };

  return (
    <View style={{padding: 20}}>
      <Text style={{fontSize: 20}}>Messages:</Text>
      {messages.map((msg, index) => (
        <Text key={index} style={{marginVertical: 5}}>
          {msg}
        </Text>
      ))}
    </View>
  );
};

export default App;
