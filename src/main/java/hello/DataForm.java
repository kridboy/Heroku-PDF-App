package hello;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

public class DataForm {
    @NotNull
    private String caseNumber;

    @NotNull
    @Min(1)
    private Integer wordCount;

    @NotNull
    @Min(1)
    private Integer briefCount;

    @NotNull
    private String sigName;

    @NotNull
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public DataForm(@NotNull String caseNumber, @NotNull @Min(1) Integer wordCount, @NotNull @Min(1) Integer briefCount, @NotNull String sigName, @NotNull LocalDate date) {
        this.caseNumber = caseNumber;
        this.wordCount = wordCount;
        this.briefCount = briefCount;
        this.sigName = sigName;
        this.date = LocalDate.now();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getWordCount() {
        return wordCount;
    }


    public Integer getBriefCount() {
        return briefCount;
    }


    public String getCaseNumber() {
        return caseNumber;
    }


    public String getSigName() {
        return sigName;
    }


    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public void setBriefCount(Integer briefCount) {
        this.briefCount = briefCount;
    }

    public void setSigName(String sigName) {
        this.sigName = sigName;
    }

    @Override
    public String toString() {
        return "DataForm{" +
                "caseNumber='" + caseNumber + '\'' +
                ", wordCount=" + wordCount +
                ", briefCount=" + briefCount +
                ", sigName='" + sigName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataForm)) return false;
        DataForm dataForm = (DataForm) o;
        return wordCount == dataForm.wordCount &&
                briefCount == dataForm.briefCount &&
                Objects.equals(caseNumber, dataForm.caseNumber) &&
                Objects.equals(sigName, dataForm.sigName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseNumber, wordCount, briefCount, sigName);
    }

    public byte[] fillData() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(new FileInputStream("src/main/resources/forms/form08.pdf"));
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(reader,writer);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf,true);
        Map<String, PdfFormField> fields = form.getFormFields();

        fields.get("form08[0].Page1[0].Signature[0]").setValue("s/"+getSigName());
        fields.get("form08[0].Page1[0].CaseNumber[0]").setValue(caseNumber);
        fields.get("form08[0].Page1[0].Words[0]").setValue(wordCount.toString());
        fields.get("form08[0].Page1[0].CourtOrderDate[0]").setValue(getDate().format(DateTimeFormatter.ofPattern("LLL d, u")));
        fields.get("form08[0].Page1[0].Date[0]").setValue(getDate().format(DateTimeFormatter.ofPattern("LLL d, u")));

        pdf.close();
        baos.close();

        return baos.toByteArray();
    }
}
