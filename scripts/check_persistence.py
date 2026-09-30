"""Register, stop the JVM, restart against the same DB, and log in again."""
import subprocess, tempfile, time, urllib.request, urllib.parse, http.cookiejar, re, pathlib
jar=str(pathlib.Path('target/shopverify-1.0.0.jar').resolve())
with tempfile.TemporaryDirectory() as directory:
 def start():
  log=open(pathlib.Path(directory)/'server.log','a')
  process=subprocess.Popen(['java','-jar',jar,'--server.port=18080','--spring.datasource.url=jdbc:h2:file:'+directory+'/accounts'],stdout=log,stderr=log)
  for _ in range(90):
   try:
    urllib.request.urlopen('http://localhost:18080/login',timeout=1); return process,log
   except Exception:
    if process.poll() is not None: raise RuntimeError(pathlib.Path(directory,'server.log').read_text())
    time.sleep(1)
  process.terminate(); raise RuntimeError('App failed to start')
 def browser(): return urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))
 def post(opener,path,fields):
  html=opener.open('http://localhost:18080'+path).read().decode()
  token=re.search(r'name="_csrf"[^>]*value="([^"]+)"',html).group(1)
  fields['_csrf']=token
  return opener.open('http://localhost:18080'+path,urllib.parse.urlencode(fields).encode())
 process,log=start()
 try:
  response=post(browser(),'/register',{'username':'restart_user','password':'TestPass123!','confirmation':'TestPass123!'})
  assert 'registered' in response.url
 finally: process.terminate(); process.wait(timeout=30); log.close()
 process,log=start()
 try:
  response=post(browser(),'/login',{'username':'restart_user','password':'TestPass123!'})
  assert response.url.endswith('/products'),response.url
  assert 'Our essentials' in response.read().decode()
  print('PASS: registered credentials survive a full application restart')
 finally: process.terminate(); process.wait(timeout=30); log.close()
