importScripts(
  'https://www.gstatic.com/firebasejs/9.22.1/firebase-app-compat.js',
);
importScripts(
  'https://www.gstatic.com/firebasejs/9.22.1/firebase-messaging-compat.js',
);

firebase.initializeApp({
  apiKey: 'AIzaSyCCipWA4RHTFp_DCSkq1nHJeApfPG4Y9aU',
  authDomain: 'emgcfmc.firebaseapp.com',
  databaseURL: 'https://project-id.firebaseio.com',
  projectId: 'emgcfmc',
  storageBucket: 'emgcfmc.firebasestorage.app',
  messagingSenderId: '57132620286',
  appId: '1:57132620286:web:6d406f4e38be5e935a48af',
  measurementId: 'G-JR7NTRX1NJ',
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage(function (payload) {
  console.log('[firebase-messaging-sw.js] 백그라운드 메시지 수신', payload);

  const notificationTitle = payload?.data?.title || '알림';
  const notificationOptions = {
    body: payload?.data?.body || '',
    icon: '/icon.png',
    data: {
      hpId: payload?.data?.payload || '',
    },
  };

  self.registration.showNotification(notificationTitle, notificationOptions);
});

self.addEventListener('notificationclick', function (event) {
  const hpId = event.notification.data?.hpId;
  event.notification.close();

  if (hpId) {
    const url = `/emgcRltmDtl?hpId=${hpId}&gubun=2`;

    event.waitUntil(
      clients
        .matchAll({ type: 'window', includeUncontrolled: true })
        .then((clientList) => {
          for (const client of clientList) {
            if (client.url.includes('/emgcRltmDtl') && 'focus' in client) {
              client.focus();
              return client.navigate(url);
            }
          }

          return clients.openWindow(url);
        }),
    );
  }
});
