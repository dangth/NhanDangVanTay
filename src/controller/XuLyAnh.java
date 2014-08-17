/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import model.BuffImagePixel;

import view.FormInput;
import view.FormMain;
/**
 *
 * @author j
 */
public class XuLyAnh {
    private FormMain frmMain;
    private FormInput frmInput;
    
    public XuLyAnh() {
   //     dsSV = IOFile.getData();
     
        frmMain = new FormMain();
        frmInput = new FormInput();
        
        frmMain.setVisible(true);
        frmMain.yeuCauChuanHoaMucXam(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chuanHoaMucXam(e);
            }
        });
        
        frmMain.yeuCauTangCuongChatLuongAnh(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tangCuongAnh(e);
            }
        });
        
        frmMain.yeuCauNhiPhanHoaAnh(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nhiPhanHoaAnh(e);
            }
        });
        
        frmMain.yeuCauTaoXuong(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taoXuong(e);
            }
        });
        
        frmMain.yeuCauLuuDuLieu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xemChiTiet(e);
            }
        });
        
        frmInput.yeuCauLuuThongTin(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                luuDuLieu(e);
            }
        });
    }
    
    private void chuanHoaMucXam(ActionEvent e) {
        BuffImagePixel bip = new BuffImagePixel(frmMain.getImage());
        ImageData imgData = new ImageData(bip.getData());
        bip.setData(imgData.getNormalizedGrayImage());
        frmMain.xemKetQua(bip.getBuffImage());
    }
    
    private void tangCuongAnh(ActionEvent e) {
        BuffImagePixel bip = new BuffImagePixel(frmMain.getImage());
        ImageData imgData = new ImageData(bip.getData());
        bip.setData(imgData.getEnhanceQualityImage());
        frmMain.xemKetQua(bip.getBuffImage());
    }
    
    private void nhiPhanHoaAnh(ActionEvent e) { 
        BuffImagePixel bip = new BuffImagePixel(frmMain.getImage());
        ImageData imgData = new ImageData(bip.getData());
        bip.setData(imgData.getBinaryImage());
        frmMain.xemKetQua(bip.getBuffImage());
    }
    
    private void taoXuong(ActionEvent e) { 
        BuffImagePixel bip = new BuffImagePixel(frmMain.getImage());
        ImageData imgData = new ImageData(bip.getData());
        bip.setData(imgData.getBoneImage());
        frmMain.xemKetQua(bip.getBuffImage());
    }
    
    private void xemChiTiet(ActionEvent e) { 
        BuffImagePixel bip = new BuffImagePixel(frmMain.getImage());
        ImageData imgData = new ImageData(bip.getData());
        //bip.setData(imgData.getBoneImage());
        bip.setData(imgData.getMinutiaeImage());
        frmMain.xemKetQua(bip.getBuffImage());
    }
    
    private void luuDuLieu(ActionEvent e) {
       
    }
    
    
    public static void main(String[] args) {
        new XuLyAnh();
    }
}
