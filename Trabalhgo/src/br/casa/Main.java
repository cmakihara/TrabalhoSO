package br.casa;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

	public Main() {
		sequencial();
		lerSequencial();
		blocos();
		lerBlocos();
		thread();
		lerThread();
	}

	private int i;	
	private String arquivo4k() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4096; i++) {
			sb.append("0");
		}
		return sb.toString();
	}
	private void lerArquivo(String nome) {
		File file = new File(nome);

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			while (bis.read() != -1) {
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Grava sequencial 250MB
	private void sequencial() {
		long inicio = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 262144; i++) {
			sb.append("0");
		}

		File file = new File("arquivo.byt");
		try {
			if (file.createNewFile()) {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long fim = System.currentTimeMillis();
		System.out.println("Sequencial");
		System.out.println("Gravação: " + (fim - inicio) / 1000 + " segundos.");

	}

	// Le sequencial
	private void lerSequencial() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 262144; i++) {
			sb.append("0");
		}

		File file = new File("arquivo.byt");
		try {
			if (file.createNewFile()) {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long inicio = System.currentTimeMillis();
		lerArquivo("arquivo.byt");
		long fim = System.currentTimeMillis();
		System.out.println("Leitura: " + (fim - inicio) / 1000 + " segundos.");
	}

	// Grava blocos
	private void blocos() {
		long inicio = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 262144; i++) {
			sb.append("0");
		}
		for (int i = 0; i < 262144; i++) {

			File file = new File("blocos.byt");
			try {
				if (file.createNewFile()) {
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(sb.toString().getBytes());
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		long fim = System.currentTimeMillis();
		System.out.println("\nBlocos");
		System.out.println("Gravação: " + (fim - inicio) / 1000 + " segundos.");

	}

	//Le blocos
	private void lerBlocos() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 262144; i++) {
			sb.append("0");
		}
		File file = new File("bloco.byt");
		try {
			if (file.createNewFile()) {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long inicio = System.currentTimeMillis();
		
		lerArquivo("bloco.byt");
		
		long fim = System.currentTimeMillis();
		System.out.println("Leitura: " + (fim - inicio) / 1000 + " segundos.");
	}

	// Grava em thread
	private void thread() {
		long inicio = System.currentTimeMillis();
		ExecutorService es = Executors.newFixedThreadPool(64);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 262144; i++) {
			sb.append("0");
		}

		for (i = 0; i < 64; i++) {
			es.execute(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < (4096); j++) {
						File file = new File("tbloco" + i + ".byt");
						try {
							if (file.createNewFile()) {
								FileOutputStream fos = new FileOutputStream(file);
								fos.write(sb.toString().getBytes());
								fos.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
			});
		}
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long fim = System.currentTimeMillis();
		System.out.println("\nThread");
		System.out.println("Gravação: " + (fim - inicio) / 1000 + " segundos.");

	}

	// Le em thread
	private void lerThread() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 262144; i++) {
			sb.append("0");
		}
		File file = new File("tbloco.byt");
		try {
			if (file.createNewFile()) {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long inicio = System.currentTimeMillis();
		ExecutorService es = Executors.newFixedThreadPool(64);

		for (i = 0; i < 64; i++) {
			es.execute(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < (262144 / 64); j++) {
						lerArquivo("tbloco.byt");
					}
				}
			});
		}
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long fim = System.currentTimeMillis();
		System.out.println("Leitura: " + (fim - inicio) / 1000 + " segundos.");
	}

	public static void main(String[] args) {
		new Main();
	}
}