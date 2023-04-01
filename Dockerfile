FROM nginx

COPY conf/nginx.conf /etc/nginx/nginx.conf
COPY /etc/letsencrypt/live/j8a802.p.ssafy.io/fullchain.pem /etc/nginx/
COPY /etc/letsencrypt/live/j8a802.p.ssafy.io/privkey.pem /etc/nginx/

CMD ["nginx", "-g", "daemon off;"]

EXPOSE 80
