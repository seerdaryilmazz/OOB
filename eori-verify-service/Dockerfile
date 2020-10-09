FROM node:boron-alpine
ADD app.tar.gz /app/
ADD config.tar.gz /nodejs-services-config/
RUN cd /nodejs-services-config/ && npm install --production
RUN cd /app/ && npm install --production
EXPOSE 3000
ENTRYPOINT ["node","/app/app.js"]