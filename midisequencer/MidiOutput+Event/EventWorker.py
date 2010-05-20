import timeimport threading ''' - Class: EventWorker- Description:This class is a worker which calls every tick all registered callbackfunction which are stored in self.__callbacks()This class needs a EventManager. If instantiated without EventManagerreference -> an exception is thrown.'''class EventWorker(threading.Thread): # EventWorker runs in its own Thread	''' Parameters		ticktime is the time in seconds between two ticks. default = 1	'''	def __init__(self, **kwargs):				kwargs.setdefault('logging', True)		self.__logging = kwargs.get('logging')		self.__log('initializing EventWorker')				# get reference to manager. if no -> exception		self.manager=kwargs.get('manager')		if self.manager==None:			raise Exception('EventWorker must be instantiated with an associated EventManager!')				# mandatory for threading		threading.Thread.__init__(self) 				# dictionary for callback methods <obj>:<callback>		self.__callbacks={}				# Event for stopping the thread so he can get new configurations		self.refreshConfig = threading.Event()		self.refreshConfig.set() # set it for fetching config initially				''' tunnel for log messages '''	def __log(self, msg):		if(self.__logging):			print 'EventWorker:\t' + msg			''' get configuration from EventManager. He knows which objects has to be called. '''	def __fetchConfig(self):		self.__log('fetching settings from EventManager')		self.__callbacks=self.manager.getCallbacks()				''' mainloop '''	def run(self): 			tickNo=0		max_i=63				playdata = []				while 1:	# BAO DEVELOP: i<5 gemacht, damit nicht so oft durchgelaufen wird			playdata = []						# could use modulo arithmetic but I fear that tickNo breaks its limits -> reset it			tickNo=tickNo+1			if(tickNo>max_i):				tickNo=0						# look if there are new settings vor import			if self.refreshConfig.isSet():			# BAO DEVELOP: Ich weiss nicht wie Thread-Safe das ist..... 				self.refreshConfig.clear()				self.__fetchConfig()							self.__log('===================== new tick: '+str(tickNo)+'=====================')			# Iterate all callback methods			for obj in self.__callbacks.keys():				self.__log('calling Callback for ' + obj.id)				tickdata = self.__callbacks[obj](tickNo)				if(tickdata!=None):					playdata.extend(tickdata)																self.__log('put playdata into queue')			self.manager.playDataQueue.put(playdata)